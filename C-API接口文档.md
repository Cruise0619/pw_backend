# PW Backend C-API 接口文档

> 基础URL: `http://localhost:端口号/api`

---
test CICD

## 通用说明

### 通用响应格式

#### ApiResponse 格式（大多数接口）
```json
{
  "success": true,
  "message": "操作成功消息（可选）",
  "data": { /* 实际数据 */ }
}
```

#### Map 格式响应（部分接口）
```json
{
  "success": true,
  "userInfo": { /* 用户信息 */ },
  "isOps": false
}
```

### 错误处理
- HTTP状态码 `4xx`: 请求参数错误或资源不存在
- HTTP状态码 `200`: 请求成功
- 响应体中的 `error` 字段包含具体错误信息

---

## 1. 用户接口 `/api/user`

### 1.1 获取所有用户
```
GET /api/user/all
```

**响应**: `List<User>`

### 1.2 用户登录/注册
```
POST /api/user/login
Content-Type: application/json

Request Body:
{
  "openid": "微信用户openid"
}

Response:
{
  "success": true,
  "userId": "openid",
  "userInfo": { /* User对象 */ },
  "isOps": false
}
```

### 1.3 获取用户信息
```
GET /api/user/{openid}
```

**响应**:
```json
{
  "success": true,
  "userInfo": { /* User对象 */ },
  "isOps": false
}
```

### 1.4 更新用户信息
```
PUT /api/user/{openid}
Content-Type: application/json

Request Body:
{
  "nickname": "用户昵称",
  "avatarUrl": "头像URL",
  "phone": "手机号"
}
```

### 1.5 检查用户是否为客服
```
GET /api/user/{openid}/isOps
```

**响应**:
```json
{
  "success": true,
  "isOps": true/false
}
```

### User 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id，与openid相同 |
| openid | String | 微信openid |
| nickname | String | 用户昵称 |
| avatarUrl | String | 用户头像URL |
| phone | String | 用户手机号 |
| registerTime | Date | 注册时间 |

---

## 2. 订单接口 `/api/orders`

### 2.1 获取所有订单
```
GET /api/orders
```

**响应**: `ApiResponse<List<Order>>`

### 2.2 根据ID获取订单
```
GET /api/orders/{id}
```

**响应**: `ApiResponse<Order>`

### 2.3 根据用户openid获取订单
```
GET /api/orders/user/{openid}
```

### 2.4 根据用户openid和状态获取订单
```
GET /api/orders/user/{openid}/status/{status}
```
**参数说明**:
- `status`: 订单状态（pre, 未开始, 待支付, 进行中, 已完成, 已取消）

### 2.5 根据状态获取订单
```
GET /api/orders/status/{status}
```

### 2.6 根据玩家ID获取订单
```
GET /api/orders/player/{playerId}
```

### 2.7 创建订单
```
POST /api/orders
Content-Type: application/json

Request Body: Order对象
```

### 2.8 更新订单状态
```
PUT /api/orders/{id}/status?status={status}
```

### 2.9 更新订单支付状态
```
PUT /api/orders/{id}/pay?paid={paid}
```
**参数**: `paid` - true/false

### 2.10 取消订单
```
PUT /api/orders/{id}/cancel
```

### 2.11 保存/更新订单
```
PUT /api/orders
Content-Type: application/json

Request Body: Order对象
```

### 2.12 删除订单
```
DELETE /api/orders/{id}
```

### Order 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id |
| openid | String | 用户openid |
| status | String | 订单状态 |
| game | String | 游戏类型 |
| items | List<OrderItem> | 商品列表 |
| players | List<String> | 玩家ID列表 |
| totalFee | BigDecimal | 总费用 |
| originalFee | BigDecimal | 原始费用 |
| discount | String | 折扣名称 |
| createTime | Date | 创建时间 |
| payTime | Date | 支付时间 |
| completeTime | Date | 完成时间 |
| waitingForCustomStart | Integer | 等待客服开始 |
| waitingForCustomCompelete | Integer | 等待客服完成 |
| waitingForPlayerStart | Integer | 等待玩家开始 |
| waitingForPlayerCompelete | Integer | 等待玩家完成 |
| waitingForCancelCorfirm | Integer | 等待取消确认 |
| waitingForWXPay | Integer | 等待微信支付 |
| waitingForScore | Integer | 等待评分 |
| isScored | Boolean | 是否已评分 |

**OrderItem 内部类**:
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | 商品ID |
| num | Integer | 数量，默认1 |

---

## 3. 商品接口 `/api/items`

### 3.1 获取所有商品
```
GET /api/items
```

**响应**: `ApiResponse<List<Item>>`

### 3.2 获取随机商品
```
GET /api/items/random?count={count}
```
**参数**: `count` - 返回数量，默认6

### 3.3 根据游戏类型获取随机商品
```
GET /api/items/random/{game}?count={count}
```

### 3.4 根据ID获取商品
```
GET /api/items/{id}
```

### 3.5 根据游戏类型获取商品
```
GET /api/items/game/{game}
```

### 3.6 根据分类获取商品
```
GET /api/items/category/{category}
```

### 3.7 根据游戏类型和分类获取商品
```
GET /api/items/game/{game}/category/{category}
```

### 3.8 根据游戏类型获取商品（排除分类）
```
GET /api/items/game/{game}/exclude?categories={categories}
```
**参数**: `categories` - 逗号分隔的分类列表

### 3.9 获取所有游戏类型
```
GET /api/items/games
```

**响应**: `ApiResponse<List<String>>`

### 3.10 保存商品
```
POST /api/items
Content-Type: application/json

Request Body: Item对象
```

### 3.11 更新商品
```
PUT /api/items
Content-Type: application/json

Request Body: Item对象
```

### 3.12 删除商品
```
DELETE /api/items/{id}
```

### Item 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id |
| title | String | 商品标题 |
| describe | String | 商品描述（简短） |
| detail | String | 商品详情 |
| price | String | 商品价格 |
| sales | String | 销量 |
| game | String | 游戏类型：cs2, val, delta, lol |
| categories | List<String> | 分类列表 |
| rules | Map<String, Double> | 购买规则 |

---

## 4. 陪玩玩家接口 `/api/players`

### 4.1 获取所有陪玩玩家
```
GET /api/players
```

**响应**: `ApiResponse<List<MockPlayer>>`

### 4.2 获取已认证的陪玩玩家
```
GET /api/players/approved
```

### 4.3 根据ID获取玩家
```
GET /api/players/{id}
```

### 4.4 根据游戏ID获取已认证玩家
```
GET /api/players/game/{gameId}
```

### 4.5 根据游戏ID和分类获取玩家
```
GET /api/players/game/{gameId}/category/{category}
```

### 4.6 保存玩家
```
POST /api/players
Content-Type: application/json

Request Body: MockPlayer对象
```

### 4.7 删除玩家
```
DELETE /api/players/{id}
```

### MockPlayer 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id |
| nickName | String | 玩家昵称 |
| wxid | String | 微信号 |
| authStatus | String | 认证状态：approved |
| games | List<PlayerGame> | 游戏信息列表 |

**PlayerGame 内部类**:
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | 游戏ID |
| name | String | 游戏名称 |
| platform | String | 平台 |
| rank | String | 段位 |
| categories | List<String> | 支持的分类 |
| scores | ScoreInfo | 评分信息 |

**ScoreInfo 内部类**:
| 字段 | 类型 | 说明 |
|------|------|------|
| score | Double | 总分 |
| count | Integer | 评分次数 |

---

## 5. 评分接口 `/api/scores`

### 5.1 获取所有评分记录
```
GET /api/scores
```

**响应**: `ApiResponse<List<ScoreRecord>>`

### 5.2 根据ID获取评分记录
```
GET /api/scores/{id}
```

### 5.3 根据玩家ID获取评分记录
```
GET /api/scores/player/{playerId}
```

### 5.4 根据订单ID获取评分记录
```
GET /api/scores/order/{orderId}
```

### 5.5 根据玩家ID和游戏类型获取评分记录
```
GET /api/scores/player/{playerId}/game/{gameType}
```

### 5.6 提交评分
```
POST /api/scores
Content-Type: application/json

Request Body: ScoreRecord对象
```

### 5.7 删除评分记录
```
DELETE /api/scores/{id}
```

### ScoreRecord 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id |
| userOpenId | String | 用户openid |
| orderId | String | 订单ID |
| playerId | String | 玩家ID |
| playerName | String | 玩家昵称 |
| gameType | String | 游戏类型 |
| score | Double | 评分（1-5） |
| comment | String | 评价内容 |
| createTime | Date | 创建时间 |

---

## 6. 分类接口 `/api/categories`

### 6.1 获取所有分类
```
GET /api/categories
```

**响应**: `ApiResponse<List<CategoryList>>`

### 6.2 根据游戏ID获取分类列表
```
GET /api/categories/game/{gameId}
```

**响应**:
```json
{
  "success": true,
  "data": ["分类1", "分类2", ...]
}
```

### 6.3 创建/更新分类
```
POST /api/categories
Content-Type: application/json

Request Body: CategoryList对象
```

### 6.4 删除分类
```
DELETE /api/categories/{id}
```

### CategoryList 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id |
| gameId | String | 游戏ID |
| categories | List<String> | 分类列表 |

---

## 7. 折扣接口 `/api/discounts`

### 7.1 获取所有折扣
```
GET /api/discounts
```

**响应**: `ApiResponse<List<Discount>>`

### 7.2 获取当前激活的折扣
```
GET /api/discounts/active
```

**响应**:
```json
{
  "success": true,
  "data": {
    "name": "折扣名称",
    "discount": "8折",
    "startTime": "开始时间",
    "endTime": "结束时间"
  }
}
```

### 7.3 激活折扣
```
POST /api/discounts/{id}/activate
```

### 7.4 创建/更新折扣
```
POST /api/discounts
Content-Type: application/json

Request Body: Discount对象
```

### 7.5 删除折扣
```
DELETE /api/discounts/{id}
```

### Discount 实体
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | MongoDB _id |
| name | String | 折扣名称 |
| discount | String | 折扣描述（如"8折"） |
| startTime | Date | 开始时间 |
| endTime | Date | 结束时间 |

---

## 8. 首页接口 `/api/index`

### 8.1 获取首页数据
```
GET /api/index/data
```

**响应**:
```json
{
  "swiperList": [
    {
      "id": 1,
      "image": "轮播图URL",
      "text": "轮播文案"
    }
  ],
  "randomItems": [ /* 随机商品列表 */ ],
  "discount": { /* 当前折扣信息 */ }
}
```

---

## 9. 数据导入接口 `/api/import`

> ⚠️ 注意：数据导入接口会清空现有数据，请谨慎使用！

### 9.1 导入指定集合数据
```
POST /api/import/collections
Content-Type: application/json

Request Body:
{
  "collections": ["users", "items", "orders"]
}
```

### 9.2 导入所有数据
```
POST /api/import/all
```

### 9.3 检查导入状态
```
GET /api/import/status
```

---

## 游戏类型说明

| 游戏ID | 游戏名称 |
|--------|----------|
| cs2 | CS2 |
| val | Valorant |
| delta | 三角洲行动 |
| lol | 英雄联盟 |

---

## 订单状态说明

| 状态值 | 说明 |
|--------|------|
| pre | 预创建 |
| 未开始 | 订单创建，待确认 |
| 待支付 | 等待支付 |
| 进行中 | 陪玩服务进行中 |
| 已完成 | 服务完成 |
| 已取消 | 订单已取消 |

---

## 接口调用示例

### C# (Unity/Winform)
```csharp
using System.Net.Http;
using System.Text;
using System.Text.Json;

public class ApiClient
{
    private readonly string baseUrl = "http://localhost:8080/api";
    private readonly HttpClient _httpClient = new HttpClient();

    // 用户登录示例
    public async Task<UserLoginResponse> LoginAsync(string openid)
    {
        var content = new StringContent(
            JsonSerializer.Serialize(new { openid = openid }),
            Encoding.UTF8,
            "application/json"
        );

        var response = await _httpClient.PostAsync($"{baseUrl}/user/login", content);
        var json = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<UserLoginResponse>(json);
    }

    // 获取商品列表示例
    public async Task<List<Item>> GetItemsByGameAsync(string game)
    {
        var response = await _httpClient.GetAsync($"{baseUrl}/items/game/{game}");
        var json = await response.Content.ReadAsStringAsync();
        var apiResponse = JsonSerializer.Deserialize<ApiResponse<List<Item>>>(json);
        return apiResponse?.data ?? new List<Item>();
    }
}
```

### C (libcurl)
```c
#include <curl/curl.h>
#include <string.h>

// GET请求示例
CURL *curl = curl_easy_init();
if(curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://localhost:8080/api/items");
    curl_easy_perform(curl);
    curl_easy_cleanup(curl);
}

// POST请求示例
struct curl_slist *headers = NULL;
headers = curl_slist_append(headers, "Content-Type: application/json");

curl_easy_setopt(curl, CURLOPT_URL, "http://localhost:8080/api/user/login");
curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "{\"openid\":\"test123\"}");
curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
curl_easy_perform(curl);
```

### C++ (cpprestsdk)
```cpp
#include <cpprest/http_client.h>
#include <cpprest/json.h>

using namespace web;
using namespace web::http;
using namespace web::http::client;

// 获取商品列表
pplx::task<void> GetItemsAsync()
{
    http_client client(U("http://localhost:8080/api"));
    
    return client.request(methods::GET, U("/items"))
        .then([](http_response response) {
            return response.extract_json();
        })
        .then([](json::value body) {
            // 处理响应数据
            auto data = body.at(U("data"));
            // ...
        });
}
```

### Unreal Engine (C++)
```cpp
#include "HttpModule.h"
#include "Interfaces/IHttpResponse.h"

void UMyBPLibrary::GetItems(FOnItemsResponse& OnSuccess, FOnError& OnError)
{
    TSharedRef<IHttpRequest> Request = FHttpModule::Get().CreateRequest();
    Request->SetURL(TEXT("http://localhost:8080/api/items"));
    Request->SetVerb("GET");
    Request->OnProcessRequestComplete().BindLambda([OnSuccess, OnError](
        FHttpRequestPtr Request,
        FHttpResponsePtr Response,
        bool bConnectedSuccessfully)
    {
        if (bConnectedSuccessfully)
        {
            TSharedPtr<FJsonObject> JsonObject;
            FJsonSerializer::Deserialize(
                TJsonReaderFactory<>::Create(Response->GetContentAsString()),
                JsonObject
            );
            // 处理响应
            OnSuccess.Broadcast();
        }
        else
        {
            OnError.Broadcast();
        }
    });
    Request->ProcessRequest();
}
```

### Unity (C#)
```csharp
using UnityEngine;
using UnityEngine.Networking;

public class ApiManager : MonoBehaviour
{
    private const string BASE_URL = "http://localhost:8080/api";

    public IEnumerator Login(string openid)
    {
        string url = $"{BASE_URL}/user/login";
        string jsonBody = $"{{\"openid\":\"{openid}\"}}";
        
        using (UnityWebRequest request = new UnityWebRequest(url, "POST"))
        {
            request.SetRequestHeader("Content-Type", "application/json");
            request.uploadHandler = new UploadHandlerRaw(
                System.Text.Encoding.UTF8.GetBytes(jsonBody)
            );
            request.downloadHandler = new DownloadHandlerBuffer();
            
            yield return request.SendWebRequest();
            
            if (request.result == UnityWebRequest.Result.Success)
            {
                Debug.Log(request.downloadHandler.text);
            }
            else
            {
                Debug.LogError(request.error);
            }
        }
    }
}
```
