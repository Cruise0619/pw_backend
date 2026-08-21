import json
import os

# 定义正确的玩家数据
players = [
    {"id": "2c74838968d4be24002331f54d1f5469", "nickName": "游戏仔", "wxid": "LL2639601354", "authStatus": "approved", "games": [{"id": "delta", "name": "Delta", "platform": "国服", "rank": "三角洲玩家", "scores": {"score": 5.0, "count": 1.0}, "categories": ["代肝", "男", "手偶"]}]},
    {"id": "2d63818a6895f0be01142d6a4a3fc411", "nickName": "K宇", "wxid": "zjm_kedi", "authStatus": "approved", "games": [{"platform": "完美", "rank": "黄金S", "categories": ["业务通", "娱乐局", "上分大神", "男"], "id": "cs2", "name": "CS2", "scores": {"count": 1.0, "score": 5.0}}]},
    {"id": "486be3ad6880d5a205f55d8424061fac", "nickName": "Cany0n", "wxid": "A1783442620", "authStatus": "approved", "games": [{"id": "lol", "name": "LOL", "platform": "国服", "rank": "最强王者", "categories": ["业务通", "娱乐局", "男"], "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "5377d5c06880d99f05fecaa14a71a9cd", "nickName": "狗皮", "wxid": "PIPIYA373", "authStatus": "approved", "games": [{"scores": {"count": 2.0, "score": 5.0}, "categories": ["娱乐局", "女"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "B+"}]},
    {"id": "557814e16880ed1c05f98fdf1401d9cc", "nickName": "Ben", "wxid": "bencheung330", "authStatus": "approved", "games": [{"rank": "玩家", "categories": ["娱乐局", ""], "id": "lol", "name": "LOL", "platform": "官服", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "6c2530cc6880da4a05fb32362d82e4f1", "nickName": "西北狼", "wxid": "ywf_0515", "authStatus": "approved", "games": [{"name": "CS2", "platform": "完美", "rank": "C+", "scores": {"score": 5.0, "count": 2.0}, "categories": ["娱乐局", "男"], "id": "cs2"}]},
    {"id": "77cd31ad68835b2b001274491ccbbf9f", "nickName": "Cherry", "wxid": "Z18698667516qr", "authStatus": "approved", "games": [{"scores": {"count": 2.0, "score": 5.0}, "categories": ["业务通", "娱乐局", "男", "陪玩", "护航", "代肝", "味道卡"], "id": "delta", "name": "Delta", "platform": "国服", "rank": "三角洲玩家"}]},
    {"id": "c611b9466880ef6105ff5e5714f53242", "nickName": "明月第一突破手", "wxid": "tbl2037938824", "authStatus": "approved", "games": [{"categories": ["娱乐局", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "B+", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "d494348b68b945cf011b88b07dbcd754", "nickName": "清秋", "wxid": "liwannuo271828", "authStatus": "approved", "games": [{"rank": "黄金", "scores": {"count": 1.0, "score": 5.0}, "categories": ["娱乐局", "女"], "id": "val", "name": "无畏契约", "platform": "国服"}]},
    {"id": "d77d384f6880d6fd0600860f796102ca", "nickName": "橘子酱", "wxid": "L-Eoupria", "authStatus": "approved", "games": [{"categories": ["业务通", "娱乐局", "男", "陪玩", "护航", "味道卡"], "id": "delta", "name": "Delta", "platform": "国服", "rank": "三角洲玩家", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "d77d384f6880d8920600a18c4f549fbb", "nickName": "包子", "wxid": "Dem0n12_27", "authStatus": "approved", "games": [{"categories": ["娱乐局", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "黄金S", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "dd1eb2b46880a78e05fae4b53ad5d565", "nickName": "小七", "wxid": "tt81775309", "authStatus": "approved", "games": [{"categories": ["业务通", "娱乐局", "上分大神", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "青铜S", "scores": {"count": 1.0, "score": 5.0}}, {"name": "Delta", "platform": "国服", "rank": "三角洲玩家", "scores": {"count": 2.0, "score": 5.0}, "categories": ["业务通", "娱乐局", "男", "陪玩", "护航", "代肝", "味道卡"], "id": "delta"}]},
    {"id": "dd1eb2b46880e9c805ff479976670dda", "nickName": "R1sk.", "wxid": "sqks597", "authStatus": "approved", "games": [{"rank": "B+", "categories": ["娱乐局", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "scores": {"score": 5.0, "count": 1.0}}, {"categories": ["娱乐局", "男"], "id": "val", "name": "val", "platform": "国服", "rank": "黄金", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "e647148e6880edb405f960ea6ad048c5", "nickName": "AerithQAQ", "wxid": "cgl1344932436", "authStatus": "approved", "games": [{"categories": ["业务通", "娱乐局", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "青铜S", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "ed74da8968b4fe4c00a517a9710a9ba6", "nickName": "小剑鱼", "wxid": "wzx1996331", "authStatus": "approved", "games": [{"categories": ["业务通", "娱乐局", "上分大神", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "青铜S", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "ed863e826880d64506027b976f9695ad", "nickName": "ma0NESY", "wxid": "ma0NESY", "authStatus": "approved", "games": [{"rank": "黄金S", "categories": ["业务通", "娱乐局", "上分大神", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "ed863e826880d9000602a91f37833a78", "nickName": "星瞳", "wxid": "LSK_15323428856", "authStatus": "approved", "games": [{"id": "cs2", "name": "CS2", "platform": "完美", "rank": "B+", "categories": ["娱乐局", "男"], "scores": {"score": 5.0, "count": 1.0}}, {"id": "delta", "name": "Delta", "platform": "国服", "rank": "三角洲玩家", "categories": ["业务通", "娱乐局", "男", "陪玩", "护航", "代肝", "味道卡"], "scores": {"score": 5.0, "count": 1.0}}, {"categories": ["业务通", "娱乐局", "上分大神", "男"], "id": "lol", "name": "LOL", "platform": "国服", "rank": "黄金", "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "f4ec2f0f6880d9dc0601a0de7a12ab26", "nickName": "老赵", "wxid": "o1028970491", "authStatus": "approved", "games": [{"id": "cs2", "name": "CS2", "platform": "完美", "rank": "青铜S", "categories": ["业务通", "娱乐局", "上分大神", "男"], "scores": {"score": 5.0, "count": 1.0}}]},
    {"id": "f51ab3f06895f22e0113c292510f147f", "nickName": "清风", "wxid": "WhosRookie", "authStatus": "approved", "games": [{"scores": {"count": 1.0, "score": 5.0}, "categories": ["娱乐局", "男"], "id": "val", "name": "无畏契约", "platform": "国服", "rank": "黄金"}]},
    {"id": "f97e292f6895f5f7011a5a0f6935817e", "nickName": "高井", "wxid": "HUAshan0777", "authStatus": "approved", "games": [{"rank": "黄金S", "scores": {"count": 1.0, "score": 5.0}, "categories": ["业务通", "娱乐局", "上分大神", "男"], "id": "cs2", "name": "CS2", "platform": "完美"}]},
    {"id": "fbf3bf436880d7c305ff95364d0fb9c5", "nickName": "榛子", "wxid": "BlackWinds_Wechat", "authStatus": "approved", "games": [{"categories": ["娱乐局", "男"], "id": "cs2", "name": "CS2", "platform": "完美", "rank": "B+", "scores": {"count": 2.0, "score": 5.0}}, {"platform": "国服", "rank": "三角洲玩家", "scores": {"count": 1.0, "score": 5.0}, "categories": ["娱乐局", "男", "陪玩", "代肝", "味道卡"], "id": "delta", "name": "Delta"}]}
]

# 先删除旧文件，再创建新文件（避免编码问题）
file_path = r'D:\pw_backend\database\database_mockPlayers.json'
if os.path.exists(file_path):
    os.remove(file_path)

with open(file_path, 'w', encoding='utf-8', newline='\n') as f:
    for p in players:
        f.write(json.dumps(p, ensure_ascii=False) + '\n')

# 验证写入
with open(file_path, 'r', encoding='utf-8') as f:
    verify_lines = f.readlines()

print(f"File written: {len(verify_lines)} lines")
for i, line in enumerate(verify_lines[:3], 1):
    data = json.loads(line.strip())
    print(f"  Line {i}: id={data['id']}, nick={data['nickName']}")
print("UTF-8 encoding verified OK!")
