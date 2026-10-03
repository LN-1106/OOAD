# 🛠️ Hướng Dẫn Thiết Lập Môi Trường Java trong vscode

## 1️⃣ Cài Đặt JDK 21 (Java Development Kit)

Dự án sử dụng **Java 21** - phiên bản Long Term Support (LTS).

### 📥 Tải & Cài Đặt

- **Oracle JDK 21:** [oracle.com](https://www.oracle.com/java/technologies/downloads/#java21)
- **OpenJDK 21:** [jdk.java.net](https://jdk.java.net/21/) (miễn phí)

### ⚙️ Cấu Hình Biến Môi Trường (Windows)

| Bước | Hành Động |
|------|----------|
| 1 | Tìm kiếm **"Edit the system environment variables"** |
| 2 | Chọn **Environment Variables** |
| 3 | Tạo biến mới: `JAVA_HOME` = `C:\Program Files\Java\jdk-21` |
| 4 | Thêm vào **Path**: `%JAVA_HOME%\bin` |

### ✅ Kiểm Tra Cài Đặt

```bash
java -version
```

Nếu hiện **Java 21** là thành công! ✓

---

## 2️⃣ Cài Đặt Apache Maven

**Maven** giúp tải thư viện tự động, không cần tải file `.jar` thủ công.

### 📥 Tải & Cài Đặt

- Tải Maven tại: [maven.apache.org](https://maven.apache.org/)
- Chọn bản **Binary Zip** (ví dụ: `apache-maven-3.9.x`)
- Giải nén vào: `C:\apache-maven-3.9.x`

### ⚙️ Cấu Hình Biến Môi Trường (Windows)

| Bước | Hành Động |
|------|----------|
| 1 | Tạo biến mới: `MAVEN_HOME` = `C:\apache-maven-3.9.x` |
| 2 | Thêm vào **Path**: `%MAVEN_HOME%\bin` |

### ✅ Kiểm Tra Cài Đặt

```bash
mvn -v
```

Nếu hiện thông tin **Apache Maven** là thành công! ✓

---

## 3️⃣ Cài Đặt Database (XAMPP & MySQL)

### 📥 Cài Đặt XAMPP

1. Tải **XAMPP** từ [apachefriends.org](https://www.apachefriends.org/)
2. Cài đặt và khởi động **XAMPP Control Panel**
3. Nhấn **Start** cho **Apache** và **MySQL**

### 📊 Import Database

```
1. Truy cập: http://localhost/phpmyadmin
2. Tạo database mới: quanlylaptop
3. Mở file: ___.sql
4. Copy toàn bộ nội dung vào thẻ SQL
5. Nhấn Go ✓
```

---

## 4️⃣ Cấu Hình Project trong VS Code

### 🔧 Bước Cài Đặt

1. **Mở dự án:** `sound_store_manage` bằng VS Code
2. **Cài Extension:** `Extension Pack for Java` (Microsoft)
   - Ctrl + Shift + X → Tìm "Extension Pack for Java" → Install

### 📦 Các Thư Viện Cần Thiết

File `pom.xml` đã được cấu hình sẵn:

| Thư Viện | Phiên Bản | Mục Đích |
|---------|----------|--------|
| **MySQL Connector** | 8.2.0 | Kết nối JDBC đến Database |
| **MigLayout** | 11.3 | Bố trí giao diện chuyên nghiệp |
| **FlatLaf** | 3.5.1 | Giao diện hiện đại & đẹp |
| **JUnit** | 4.11 | Unit Testing |

### 🎨 Cấu Hình Java Version

```xml
<properties>
  <maven.compiler.source>21</maven.compiler.source>
  <maven.compiler.target>21</maven.compiler.target>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

---

## 5️⃣ Chạy Dự Án

### ▶️ Cách 1: Dùng VS Code

1. Mở file: `src/main/java/App.java`
2. Nhấn nút **Run** (▶️) hoặc phím `F5`
3. Đợi kết nối database...

### ▶️ Cách 2: Dùng Terminal (Maven)

```bash
# Build dự án
mvn clean install

# Chạy ứng dụng
mvn exec:java -Dexec.mainClass="App"
```

### ✅ Thành Công!

Nếu Terminal hiện **✅ KẾT NỐI THÀNH CÔNG!** → Bắt đầu "chiến" thôi! 🎉

---

## 📝 Lưu Ý Quan Trọng cho Team

### ⚠️ Quy Tắc Dự Án

- ❌ **Không xóa** thư mục: `src/main/java` hay `src/main/resources` (tuân theo Maven)
- ✅ **Thêm ảnh**: Bỏ vào `src/main/resources/images`
- ✅ **Thêm config**: Bỏ vào `src/main/resources/config`

### 🆘 Gặp Lỗi?

Nếu có lỗi (lỗi đỏ trong VS Code):

```
Ctrl + Shift + P → Java: Clean Language Server Workspace → Restart
```

Hoặc xóa folder `.vscode` rồi mở lại dự án.

### 📋 Cấu Hình pom.xml (Dependencies)

```xml
<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <maven.compiler.source>21</maven.compiler.source>
  <maven.compiler.target>21</maven.compiler.target>
</properties>

<dependencies>
  <!-- Unit Testing -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
  </dependency>
  
  <!-- MySQL JDBC -->
  <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.2.0</version>
  </dependency>

  <!-- MigLayout (UI Layout) -->
  <dependency>
    <groupId>com.miglayout</groupId>
    <artifactId>miglayout-swing</artifactId>
    <version>11.3</version>
  </dependency>

  <!-- FlatLaf (Modern Look & Feel) -->
  <dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.5.1</version>
  </dependency>
</dependencies>
```

---

## 🎓 Tóm Tắt Các Bước

```
✅ JDK 21              → Cài + Cấu hình JAVA_HOME
✅ Maven 3.9.x        → Cài + Cấu hình MAVEN_HOME
✅ XAMPP + MySQL      → Cài + Chạy + Import DB
✅ VS Code + Extension → Mở dự án + Cài Extension Pack for Java
✅ Chạy dự án         → F5 hoặc mvn exec:java
```
---