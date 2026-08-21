import requests

# 导入所有数据
url = 'http://localhost:8081/api/import/all'
try:
    response = requests.post(url)
    print(f'Status: {response.status_code}')
    print(f'Response: {response.text}')
except Exception as e:
    print(f'Error: {e}')
