# 🎬 Cinema Booking Mobile App

Ứng dụng đặt vé xem phim di động được phát triển bằng Android (Java) với giao diện thân thiện và các tính năng hiện đại.

## 📱 Tính năng chính

- **Đăng ký/Đăng nhập**: Xác thực người dùng với Firebase Auth và Google Sign-In
- **Xem danh sách phim**: Hiển thị các bộ phim đang chiếu và sắp chiếu
- **Chi tiết phim**: Thông tin chi tiết về phim, trailer, đánh giá
- **Đặt vé**: Chọn suất chiếu, ghế ngồi và thanh toán
- **Quản lý đặt chỗ**: Xem lịch sử đặt vé và trạng thái đơn hàng
- **Profile**: Quản lý thông tin cá nhân và cài đặt

## 🛠 Công nghệ sử dụng

### Frontend
- **Android SDK**: Phát triển ứng dụng native
- **Java**: Ngôn ngữ lập trình chính
- **Material Design**: Thiết kế giao diện hiện đại

### Backend & Services
- **Firebase**: 
  - Authentication (đăng nhập/đăng ký)
  - Storage (lưu trữ hình ảnh)
  - Analytics (phân tích người dùng)
- **Retrofit**: HTTP client cho API calls
- **OkHttp**: Networking và logging
- **RxJava/RxAndroid**: Reactive programming

### UI/UX Libraries
- **Material Components**: Giao diện Material Design
- **Picasso/Glide**: Load và cache hình ảnh
- **CircleImageView**: Hiển thị ảnh tròn

### Other Tools
- **Gson**: JSON serialization/deserialization
- **Gradle**: Build system
- **ProGuard**: Code obfuscation

## 📋 Yêu cầu hệ thống

- **Android**: API Level 26+ (Android 8.0)
- **Target SDK**: 34
- **Java**: Version 1.8+
- **Gradle**: 8.0+

## 🚀 Hướng dẫn cài đặt

### 1. Clone repository
```bash
git clone <repository-url>
cd cinema-booking_mobile-proj
```

### 2. Cấu hình Firebase
1. Tạo project mới trên [Firebase Console](https://console.firebase.google.com/)
2. Thêm Android app với package name: `com.example.cinema_booking_mobile`
3. Tải file `google-services.json` và đặt vào thư mục `app/`
4. Cấu hình Authentication và Storage trên Firebase Console

### 3. Cấu hình Google Sign-In
1. Trong Firebase Console, enable Google Sign-In provider
2. Cấu hình SHA-1 fingerprint cho debug/release builds

### 4. Build project
```bash
# Sync dependencies
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug
```

## 📁 Cấu trúc dự án

```
app/src/main/java/com/example/cinema_booking_mobile/
├── activity/          # Các Activity chính
├── adapter/           # RecyclerView Adapters
├── dto/              # Data Transfer Objects
├── fragment/         # UI Fragments
├── model/            # Data Models
├── service/          # API Services
├── util/             # Utility classes
├── others/           # Các class khác
└── MainActivity.java # Main Entry Point
```

## 🔧 Cấu hình API

Cập nhật base URL của API trong file service:
```java
public static final String BASE_URL = "https://your-api-endpoint.com/";
```

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 📦 Build Release

1. Tạo keystore cho signing:
```bash
keytool -genkey -v -keystore cinema-release-key.keystore -alias cinema_key -keyalg RSA -keysize 2048 -validity 10000
```

2. Cấu hình signing trong `app/build.gradle.kts`

3. Build release APK:
```bash
./gradlew assembleRelease
```

## 🤝 Đóng góp

1. Fork project
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Liên hệ

- **Developer**: [Your Name]
- **Email**: [dattq.b21cn222@stu.ptit.edu.vn]
- **Project Link**: [https://github.com/yourusername/cinema-booking_mobile-proj](https://github.com/yourusername/cinema-booking_mobile-proj)

## 🎯 Roadmap

- [ ] Thêm chức năng thanh toán online
- [ ] Tích hợp thông báo đẩy
- [ ] Thêm chế độ Dark Mode
- [ ] Hỗ trợ đa ngôn ngữ
- [ ] Tối ưu hiệu suất và trải nghiệm người dùng

## 📸 Screenshots

*Thêm screenshots của ứng dụng tại đây*

---

⭐ Đừng quên star repository nếu project hữu ích!
