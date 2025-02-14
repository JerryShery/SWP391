package DAO;

import Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private final DBContext dbContext;
    private final Connection connection;

    public UserDAO() {
//        dbContext = DBContext.getInstance();
//        connection = dbContext.getConnection();
        dbContext = new DBContext();
        connection = dbContext.conn;
    }

    // Thêm người dùng mới vào database
//    public void createUser(User user) {
//        String sql = "INSERT INTO Users (first_name, last_name, phone, email, username, password, dob, gender, address, avatar, roleId, isActive, token, expired_token) "
//                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setString(1, user.getFirstName());
//            pstmt.setString(2, user.getLastName());
//            pstmt.setString(3, user.getPhone());
//            pstmt.setString(4, user.getEmail());
//            pstmt.setString(5, user.getUsername());
//            pstmt.setString(6, user.getPassword());
//            pstmt.setDate(7, new java.sql.Date(user.getDob().getTime()));
//            pstmt.setBoolean(8, user.isGender());
//            pstmt.setString(9, user.getAddress());
//            pstmt.setString(10, user.getAvatar());
//            pstmt.setInt(11, user.getRoleId());
//            pstmt.setBoolean(12, user.isIsActive());
//            pstmt.setString(13, user.getToken());
//            pstmt.setString(14, user.getExpiredToken());
//            pstmt.executeUpdate();
//            System.out.println("User created successfully!");
//        } catch (SQLException ex) {
//            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error inserting user", ex);
//        }
//    }
    public boolean createUser(User user) {
        if (checkExistUser(user.getUsername())) {
            System.out.println("Username already exists!");
            return false; // Không tạo user nếu đã tồn tại
        }

        String sql = "INSERT INTO Users (first_name, last_name, phone, email, username, password, dob, gender, address, avatar, roleId, isActive, token, expired_token) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getUsername());
            pstmt.setString(6, user.getPassword());
            pstmt.setDate(7, new java.sql.Date(user.getDob().getTime()));
            pstmt.setBoolean(8, user.isGender());
            pstmt.setString(9, user.getAddress());
            pstmt.setString(10, user.getAvatar());
            pstmt.setInt(11, user.getRoleId());
            pstmt.setBoolean(12, user.isIsActive());
            pstmt.setString(13, user.getToken());
            pstmt.setString(14, user.getExpiredToken());
            pstmt.executeUpdate();
            System.out.println("User created successfully!");
            return true; // Trả về true nếu tạo thành công
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error inserting user", ex);
        }
        return false;
    }

    public static void main(String[] args) {
        UserDAO u = new UserDAO();
        User us = new User();
        us.setUsername("hieu");
        us.setPassword("1");
        us.setRoleId(3);
        us.setDob(generateRandomDob());
        u.createUser(us);
    }

    private static java.util.Date generateRandomDob() {
        Random rand = new Random();

        // Set the range for the year
        int startYear = 1970;
        int endYear = 2000;

        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Generate a random year between startYear and endYear
        int year = rand.nextInt(endYear - startYear + 1) + startYear;
        calendar.set(Calendar.YEAR, year);

        // Generate a random month between 0 (January) and 11 (December)
        int month = rand.nextInt(12);
        calendar.set(Calendar.MONTH, month);

        // Generate a random day of the month based on the actual number of days in the selected month
        int day = rand.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) + 1;
        calendar.set(Calendar.DAY_OF_MONTH, day);

        // Return the random date
        return calendar.getTime();
    }

    // Lấy thông tin người dùng theo username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error fetching user", ex);
        }
        return null;
    }

    // Cập nhật thông tin người dùng
    public void updateUser(User user) {
        String sql = "UPDATE Users SET first_name=?, last_name=?, phone=?, email=?, password=?, dob=?, gender=?, address=?, avatar=?, roleId=?, isActive=?, token=?, expired_token=? WHERE username=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setDate(6, new java.sql.Date(user.getDob().getTime()));
            pstmt.setBoolean(7, user.isGender());
            pstmt.setString(8, user.getAddress());
            pstmt.setString(9, user.getAvatar());
            pstmt.setInt(10, user.getRoleId());
            pstmt.setBoolean(11, user.isIsActive());
            pstmt.setString(12, user.getToken());
            pstmt.setString(13, user.getExpiredToken());
            pstmt.setString(14, user.getUsername());
            pstmt.executeUpdate();
            System.out.println("User updated successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error updating user", ex);
        }
    }

    // Xóa người dùng theo username
    public void deleteUser(String username) {
        String sql = "DELETE FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("User deleted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error deleting user", ex);
        }
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error fetching users", ex);
        }
        return users;
    }

    // Kiểm tra người dùng có tồn tại với username không
    public boolean checkExistUser(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng kết quả > 0 thì người dùng đã tồn tại
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error checking user existence", ex);
        }
        return false; // Trả về false nếu không có người dùng nào
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Lấy thông tin người dùng từ database
        User user = getUserByUsername(username);
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        // Kiểm tra mật khẩu cũ có khớp không
        if (!user.getPassword().equals(oldPassword)) {
            System.out.println("Old password is incorrect!");
            return false;
        }

        // Kiểm tra mật khẩu mới và mật khẩu cũ có giống nhau không
        if (oldPassword.equals(newPassword)) {
            System.out.println("New password must be different from the old password!");
            return false;
        }

        // Cập nhật mật khẩu mới
        String sql = "UPDATE Users SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            System.out.println("Password changed successfully!");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Error changing password", ex);
        }
        return false;
    }

    // Phương thức hỗ trợ ánh xạ dữ liệu từ ResultSet sang User
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("userId"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getDate("dob"),
                rs.getBoolean("gender"),
                rs.getString("address"),
                rs.getString("avatar"),
                rs.getInt("roleId"),
                rs.getBoolean("isActive"),
                rs.getString("token"),
                rs.getString("expired_token")
        );
    }

}
