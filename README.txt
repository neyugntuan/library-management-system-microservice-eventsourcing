Hướng dẫn Chạy	

Lưu ý: Tất cả lệnh bash cần đứng tại thư mục chính

Bước 1: Cài Kubernetes

Bước 2:

Tại thư mục chính, mở terminal chạy:

minikube start --profile=minikube --driver=docker --memory=16384 --cpus=6

Chờ minikube start xong

Bước 3: Build các service lên docker

Qua terminal khác, vẫn tại thư mục chính, chạy

docker compose build

(Quá trình có thể mất tới 30 phút đến 1 giờ)

Bước 4: Đẩy các image lên k8s

Lần lượt chạy:

#Discoveryserver
minikube image load library-management-system-microservice-eventsourcing-discoveryserver:latest

# Kafka
minikube image load confluentinc/cp-server:7.7.0

# Zookeeper
minikube image load confluentinc/cp-zookeeper:7.7.0

# Kafka Control Center
minikube image load confluentinc/cp-enterprise-control-center:7.7.0

# Redis
minikube image load redis:latest

# Keycloak
minikube image load quay.io/keycloak/keycloak:25.0.0

# Axon Server
minikube image load axoniq/axonserver:latest


#Bookservice
minikube image load library-management-system-microservice-eventsourcing-bookservice:latest

#Employeeservice
minikube image load library-management-system-microservice-eventsourcing-employeeservice:latest

#Borrowingservice
minikube image load library-management-system-microservice-eventsourcing-borrowingservice:latest

#Userservice
minikube image load library-management-system-microservice-eventsourcing-userservice:latest

#Notificationservice
minikube image load library-management-system-microservice-eventsourcing-notificationservice:latest

#Apigateway
minikube image load library-management-system-microservice-eventsourcing-apigateway:latest



Bước 5: apply các manifest

	kubectl apply -R -f .\k8s\

Gõ minikube dashboard để truy cập vào dashboard k8s

	minikube dashboard

PROB: Lần đầu apply, Userservice sẽ bị lỗi, do thiếu cấu hình client secret của keycloak, cũng như không kết nối được database.

PROVE: 
1)	DATABASE
	Vào Xampp bật Apache và MySQL, ấn vào nút admin của MySQL để vào trang quản trị.

	Tạo 1 database tên neyugntuan lỗi sẽ tự hết, Kubernetes sẽ tự reset và server sẽ tự làm phần còn lại

2)	Keycloak
	Trong terminal gõ: Minikube service keycloak –url

	Truy cập vào URL, đăng nhập tài khoản và mật khẩu: admin

	Ấn Create Realm, và import file realm-export.json vào.

	Vào Realm neyugntuan > client > neyugntuan-app > credencial > client-secret

	Ấn nút regenerate và copy lại vào notepad

	Vào thư mục chính > \userservice\src\main\resources> application.yml

	Kéo xuống cuối file sửa giá trị client-secret: {chuỗi nãy đã copy vào notepad}

Gõ:

	minikube image load library-management-system-microservice-eventsourcing-userservice:latest

	kubectl apply -R -f .\k8s\08-userservice\

	đợi start lên lần nữa, thấy log "admin user has been created with defauft password: admin, please change it" là thành công

Lưu ý: mọi truy cập API không được đi trực tiếp qua service gốc mà phải qua Apigateway

Bước 6: Tài liệu API

Để ép cổng:  kubectl port-forward service/apigateway 8080:8080

	http://localhost:8080

tương tự : kubectl port-forward service/bookservice 9001:9001

	http://localhost:9001

Để truy cập vào API DOCUMENTATION bạn thêm hậu tố /swagger-ui/index.html

vd: http://localhost:9001/swagger-ui/index.html

Trong đó là tài liệu API đầy đủ 


Tương tự 

paste: kubectl port-forward service/employeeservice 9002:9002

 truy cập: http://localhost:9002/swagger-ui/index.html

paste: kubectl port-forward service/borrowingservice 9004:9004

 truy cập: http://localhost:9004/swagger-ui/index.html

paste: kubectl port-forward service/userservice 9005:9005

 truy cập: http://localhost:9005/swagger-ui/index.html


