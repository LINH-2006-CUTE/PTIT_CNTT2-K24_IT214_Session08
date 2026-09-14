# Rikkei Commerce Microservices

Mini project thương mại điện tử được phát triển từ code mẫu Session 06 và bám đúng phạm vi yêu cầu: Database-per-service, Config Server, Eureka, API Gateway, Spring Cloud LoadBalancer và giao tiếp đồng bộ OpenFeign.

## Kiến trúc

| Thành phần | Cổng | Database / trách nhiệm |
|---|---:|---|
| Config Server | 8888 | Đọc cấu hình tập trung từ `config-repo/` |
| Eureka Server | 8761 | Đăng ký và khám phá service |
| API Gateway | 8080 | Điểm truy cập duy nhất của client |
| Customer Service | 8091 | `customer_db` — khách hàng |
| Product Service | 8092 | `product_db` — danh mục sản phẩm |
| Inventory Service | 8093 | `inventory_db` — tồn kho và giữ hàng |
| Order Service | 8094 | `order_db` — đơn hàng; tổng hợp dữ liệu qua Feign |
| Payment Service | 8095 | `payment_db` — thanh toán |

Client chỉ gọi `http://localhost:8080`. Các lệnh gọi nội bộ dùng tên như `product-service`; Eureka cung cấp danh sách instance và Spring Cloud LoadBalancer chọn instance, nên thêm/bớt bản sao không cần sửa code bên gọi.

## Chạy dự án

Yêu cầu: Java 21, Docker Desktop.

1. Khởi động năm database độc lập:

   ```bash
   docker compose up -d
   ```

2. Chạy lần lượt từng lệnh dưới đây ở các terminal riêng, từ thư mục gốc dự án:

   ```bash
   ./gradlew :config-service:bootRun
   ./gradlew :eureka-service:bootRun
   ./gradlew :customer-service:bootRun
   ./gradlew :product-service:bootRun
   ./gradlew :inventory-service:bootRun
   ./gradlew :order-service:bootRun
   ./gradlew :payment-service:bootRun
   ./gradlew :gateway-service:bootRun
   ```

Đợi mỗi tầng sẵn sàng trước khi chạy tầng tiếp theo: Config → Eureka → nghiệp vụ → Gateway. Mở `http://localhost:8761` để kiểm tra các instance đã đăng ký.

Nếu dùng IntelliJ, chạy các lớp `*Application` theo đúng thứ tự trên. Tất cả bảng và dữ liệu mẫu được tạo tự động khi service khởi động lần đầu.

## Kiểm thử nhanh

Import hai file trong thư mục `postman/`, chọn environment **Rikkei Commerce Local**, rồi chạy collection theo thứ tự. Collection tự lưu ID đơn mới để các request thanh toán và xem tổng quan sử dụng tiếp.

API thể hiện yêu cầu quan trọng nhất:

```http
GET http://localhost:8080/api/v1/orders/1/summary
```

Kết quả gồm đơn hàng, thông tin khách, từng sản phẩm, tồn kho hiện tại và thanh toán — dữ liệu nằm ở năm database nhưng client chỉ tải một lần qua một địa chỉ.

## Tăng thêm instance

Ví dụ chạy thêm một Product Service ở cổng 8192:

```bash
PORT=8192 ./gradlew :product-service:bootRun
```

Instance mới tự đăng ký với Eureka. Gateway và Order Service tiếp tục dùng `lb://product-service` / Feign theo tên service, không đổi cấu hình phía gọi.

## Cấu hình tập trung

Mọi cấu hình môi trường nằm trong `config-repo/`. Có thể đổi cổng, URL database, timeout Feign hoặc route tại đây mà không sửa mã nguồn service. Khi chạy file JAR từ một thư mục khác, đặt biến `CONFIG_REPO` thành đường dẫn tuyệt đối tới thư mục này. Với native Config Server trong phạm vi bài học, service nhận cấu hình mới sau khi khởi động lại; không cần build lại. Cơ chế phát tán cấu hình tức thời bằng message bus nằm ngoài phạm vi đề bài.

## Kiểm thử mã nguồn

```bash
./gradlew clean test
```

Xem [phân tích kiến trúc](docs/architecture.md) để dùng khi thuyết trình/bảo vệ bài.
