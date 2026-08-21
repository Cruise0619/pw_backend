import urllib.request, sys
sys.stdout.reconfigure(encoding='utf-8')

# 测试用户接口
try:
    r = urllib.request.urlopen('http://localhost:8081/api/user/user_1776925976882_dh00l8isb')
    print("OK User API:", r.read().decode()[:200])
except Exception as e:
    print("FAIL User API:", e)

# 测试图片
try:
    r = urllib.request.urlopen('http://localhost:8081/images/right-arrow.png')
    data = r.read()
    print(f"OK right-arrow.png: {len(data)} bytes")
except Exception as e:
    print("FAIL Image:", e)
