# Phân tích và bảo vệ kiến trúc

## Vì sao không tiếp tục monolith?

Ở hệ thống cũ, mọi module cùng codebase, quy trình build/deploy và database. Một thay đổi nhỏ buộc triển khai lại toàn bộ, phạm vi lỗi lớn, các đội chờ nhau và không thể chỉ tăng tài nguyên cho chức năng đang nóng. Tách theo ranh giới Customer, Catalog, Inventory, Order và Payment giúp từng đội sở hữu trọn service lẫn schema, phát triển và triển khai độc lập.

Microservices cũng tạo thêm chi phí: nhiều tiến trình phải vận hành, cuộc gọi mạng có thể chậm hoặc lỗi, và không có transaction ACID xuyên nhiều database. Vì vậy dự án chỉ tách theo năng lực nghiệp vụ rõ ràng, không tách mỗi bảng thành một service.

## Trả lời các câu hỏi của PM

1. **Ai tổng hợp dữ liệu?** Order Service là composition service vì nó sở hữu nghiệp vụ đơn hàng và biết dữ liệu nào tạo nên màn hình chi tiết đơn. Gateway chỉ định tuyến, không chứa nghiệp vụ. Client không phải gọi nhiều API.
2. **Ảnh hưởng của gọi đồng bộ?** Độ trễ tổng bằng các lần gọi phụ thuộc; một service chậm có thể làm request tổng hợp chậm hoặc thất bại. Dự án đặt connect timeout 3 giây, read timeout 5 giây và trả lỗi thay vì chờ vô hạn. Circuit breaker, cache phân tán và event-driven là hướng mở rộng nhưng nằm ngoài phạm vi đề.
3. **RestTemplate hay FeignClient?** Chọn OpenFeign vì interface khai báo ngắn, dễ đọc, tích hợp Eureka và Spring Cloud LoadBalancer theo tên service. Đây là bước phát triển trực tiếp từ cả hai ví dụ RestTemplate/Feign trong code mẫu.
4. **Một địa chỉ duy nhất?** API Gateway cổng 8080. Gateway dùng route `lb://service-name`, lấy instance động từ Eureka.
5. **Eureka và LoadBalancer phối hợp?** Mỗi instance tự đăng ký tên, IP và port. Bên gọi lấy danh sách instance từ registry; LoadBalancer chọn một instance cho từng request. Thêm instance mới không thay đổi URL hay code bên gọi.
6. **Cấu hình nào tập trung?** Port, datasource URL/credential, Eureka URL, Gateway routes và Feign timeout. Config Server đọc thư mục ngoài classpath `config-repo/`, nên đổi cấu hình không cần build lại service.

## Ranh giới dữ liệu

- Customer Service không biết bảng sản phẩm hay đơn hàng.
- Product Service sở hữu mô tả và giá bán hiện tại.
- Inventory Service chỉ lưu `productId` và số lượng, không truy cập `product_db`.
- Order Service lưu snapshot tên/giá tại thời điểm mua để lịch sử đơn không đổi khi catalog đổi.
- Payment Service chỉ tham chiếu `orderId` và kiểm tra đơn qua REST, không đọc `order_db`.

Không service nào khai báo datasource trỏ tới database của service khác.

## Luồng tạo và xem đơn

Tạo đơn: Gateway → Order → kiểm tra Customer → lấy Product → kiểm tra/giữ Inventory → lưu Order. Thanh toán: Gateway → Payment → lấy Order → lưu Payment → cập nhật trạng thái Order. Xem tổng quan: Gateway → Order → gọi Customer, Product, Inventory, Payment → ghép một response.

## Giới hạn đã ghi nhận

Gọi REST đồng bộ không tạo transaction phân tán. Nếu lỗi đúng lúc sau khi giữ một phần tồn kho hoặc ghi Payment, dữ liệu có thể cần đối soát thủ công. Race condition trong một Inventory instance được bảo vệ bằng khóa database, nhưng tính nguyên tử xuyên nhiều service cần Saga/message broker — các kỹ thuật mà đề bài chủ động không yêu cầu. Đây là giới hạn minh bạch của giải pháp trong phạm vi Session 02/03/05/06.

