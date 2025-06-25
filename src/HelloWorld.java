import java.util.Scanner;
import java.io.IOException; // Added missing import

public class HelloWorld {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserProfile user = null;

        System.out.println("🌟 User Profile Manager 🌟");

        while(true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Create New Profile");
            System.out.println("2. Save Current Profile");
            System.out.println("3. Load Existing Profile");
            System.out.println("4. Verify Password");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch(choice) {
                case 1:
                    user = createProfile(scanner);
                    break;
                case 2:
                    saveProfile(user, scanner);
                    break;
                case 3:
                    user = loadProfile(scanner);
                    if(user != null) user.printProfile();
                    break;
                case 4:
                    verifyPassword(user, scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static UserProfile createProfile(Scanner scanner) {
        System.out.println("\n--- Create New Profile ---");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        int age = 0;
        boolean validAge = false;
        while (!validAge) {
            System.out.print("Enter your age: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                validAge = true;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid! Enter numbers only.");
            }
        }

        String email = "";
        boolean validEmail = false;
        while (!validEmail) {
            System.out.print("Enter your email: ");
            String inputEmail = scanner.nextLine();
            if (inputEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
                email = inputEmail;
                validEmail = true;
            } else {
                System.out.println("❌ Invalid email! Use format: user@example.com");
            }
        }

        String password = "";
        boolean validPassword = false;
        while (!validPassword) {
            System.out.print("Create password: ");
            String password1 = scanner.nextLine();

            System.out.print("Confirm password: ");
            String password2 = scanner.nextLine();

            if (password1.equals(password2)) {
                if (password1.length() >= 8) {
                    password = password1;
                    validPassword = true;
                } else {
                    System.out.println("❌ Password must be at least 8 characters!");
                }
            } else {
                System.out.println("❌ Passwords don't match!");
            }
        }

        UserProfile newUser = new UserProfile(name, age, email, password);
        newUser.printProfile();
        return newUser;
    }

    private static void saveProfile(UserProfile user, Scanner scanner) {
        if(user == null) {
            System.out.println("❌ No profile to save! Create one first.");
            return;
        }

        try {
            user.saveToFile();
            System.out.println("✅ Profile saved successfully!");
        } catch (IOException e) {
            System.out.println("❌ Save failed: " + e.getMessage());
        }
    }

    private static UserProfile loadProfile(Scanner scanner) {
        System.out.print("\nEnter username to load: ");
        String username = scanner.nextLine();

        try {
            UserProfile profile = UserProfile.loadFromFile(username);
            System.out.println("✅ Profile loaded!");
            return profile;
        } catch (IOException e) {
            System.out.println("❌ Load failed: " + e.getMessage());
            return null;
        }
    }

    private static void verifyPassword(UserProfile user, Scanner scanner) {
        if(user == null) {
            System.out.println("❌ No profile loaded! Create or load one first.");
            return;
        }

        System.out.print("Enter password to verify: ");
        String testPassword = scanner.nextLine();
        System.out.println("Verification result: " + user.verifyPassword(testPassword));
    }
}
