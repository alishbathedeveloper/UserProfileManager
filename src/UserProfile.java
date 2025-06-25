import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.nio.file.*;

public class UserProfile {
    private String name;
    private int age;
    private String email;
    private String passwordHash;

    public UserProfile(String name, int age, String email, String password) {
        this.name = name;
        this.age = age;

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("❌ Invalid email format!");
        }
        this.email = email;

        this.passwordHash = hashPassword(password);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("⚠️ Password hashing failed", e);
        }
    }

    public boolean verifyPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    public String getName() { return name; }
    public int getBirthYear() { return 2025 - age; }
    public String getStatus() { return (age >= 18) ? "Adult" : "Minor"; }

    public void printProfile() {
        System.out.println("\n=== " + name + "'s Profile ===");
        System.out.println("Age: " + age);
        System.out.println("Status: " + getStatus());
        System.out.println("Birth Year: " + getBirthYear());
        System.out.println("Email: " + email);
    }

    // NEW: Save profile to file
    public void saveToFile() throws IOException {
        String filename = this.name.replace(" ", "_") + ".profile";
        Path path = Paths.get("user_profiles", filename);
        Files.createDirectories(path.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("NAME:" + name);
            writer.newLine();
            writer.write("AGE:" + age);
            writer.newLine();
            writer.write("EMAIL:" + email);
            writer.newLine();
            writer.write("PASSWORD_HASH:" + passwordHash);
        }
    }

    // NEW: Load profile from file
    public static UserProfile loadFromFile(String username) throws IOException {
        String filename = username.replace(" ", "_") + ".profile";
        Path path = Paths.get("user_profiles", filename);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String name = reader.readLine().split(":")[1];
            int age = Integer.parseInt(reader.readLine().split(":")[1]);
            String email = reader.readLine().split(":")[1];
            String passwordHash = reader.readLine().split(":")[1];

            // Create dummy profile (password not used)
            UserProfile profile = new UserProfile(name, age, email, "dummy_password");
            profile.passwordHash = passwordHash;
            return profile;
        }
    }
}