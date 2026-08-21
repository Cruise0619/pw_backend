# -*- coding: utf-8 -*-
"""恢复丢失的 score_rec 和 ops 集合，从 database/ 目录的备份文件导入。

- 保留原始 _id
- 将日期字段(registerTime/createTime)正确转成 MongoDB Date 类型
"""
import json
import re
from datetime import datetime

from pymongo import MongoClient

DATABASE_DIR = r"D:\pw_backend\database"
ISO_RE = re.compile(r"^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$")


def parse_date(value):
    """把字符串日期转成 datetime；已是 ISO 格式或带 $date 包装的都处理。"""
    if value is None:
        return None
    if isinstance(value, dict):
        # MongoDB Extended JSON: {"$date": "..."}
        value = value.get("$date", value)
    if isinstance(value, datetime):
        return value
    if isinstance(value, str):
        s = value.strip()
        try:
            return datetime.fromisoformat(s.replace("Z", "+00:00"))
        except ValueError:
            return value
    return value


def load_lines(path):
    with open(path, "r", encoding="utf-8") as f:
        docs = []
        for line in f:
            line = line.strip()
            if not line:
                continue
            docs.append(json.loads(line))
        return docs


def normalize_dates(doc, date_fields):
    for field in date_fields:
        if field in doc:
            doc[field] = parse_date(doc[field])
    return doc


def main():
    client = MongoClient("mongodb://localhost:27017", serverSelectionTimeoutMS=5000)
    db = client["pw_backend"]

    results = {}

    # 1) 恢复 score_rec（评分记录）
    score_docs = load_lines(DATABASE_DIR + r"\database_score_rec.json")
    score_docs = [normalize_dates(d, ["createTime"]) for d in score_docs]
    score_coll = db["score_rec"]
    score_coll.drop()
    if score_docs:
        score_coll.insert_many(score_docs)
    results["score_rec"] = len(score_docs)

    # 2) 恢复 ops（客服账号）
    ops_docs = load_lines(DATABASE_DIR + r"\database_ops.json")
    ops_docs = [normalize_dates(d, ["registerTime"]) for d in ops_docs]
    ops_coll = db["ops"]
    ops_coll.drop()
    if ops_docs:
        ops_coll.insert_many(ops_docs)
    results["ops"] = len(ops_docs)

    # 3) 验证
    print("=== 恢复结果 ===")
    for coll, cnt in results.items():
        actual = db[coll].count_documents({})
        print(f"  {coll}: 应导入 {cnt} 条, 实际 {actual} 条")

    print("\n=== 日期字段类型验证 ===")
    for coll, field in [("score_rec", "createTime"), ("ops", "registerTime")]:
        sample = db[coll].find_one()
        if sample:
            val = sample.get(field)
            print(f"  {coll}.{field} = {val!r} -> 类型: {type(val).__name__}")

    print("\n=== 全部集合最终状态 ===")
    for name in sorted(db.list_collection_names()):
        print(f"  {name}: {db[name].count_documents({})} 条")


if __name__ == "__main__":
    main()
