import java.io.IOException;
import java.util.Scanner;

public class MedicalAssistantApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PatientAuth auth = new PatientAuth();

        while (true) {
            System.out.println("\n========================================");
            System.out.println("       🩺 MEDICAL ASSISTANT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. 📝 Register as New Patient");
            System.out.println("2. 🔐 Login and Diagnose");
            System.out.println("3. ❌ Exit");
            System.out.print("Enter your choice (1-3): ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    try {
                        System.out.print("Enter Patient ID: ");
                        String id = sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Set Password: ");
                        String password = sc.nextLine();

                        if (auth.registerPatient(id, name, password)) {
                            System.out.println("✅ Registration successful!");
                        } else {
                            System.out.println("⚠️ Patient ID already exists!");
                        }
                    } catch (IOException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case "2":
                    try {
                        System.out.print("Enter Patient ID: ");
                        String id = sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Password: ");
                        String password = sc.nextLine();

                        if (auth.authenticate(id, name, password)) {
                            System.out.println("\n✅ Login successful!");
                            MedicalDataBase db = new MedicalDataBase();

                            System.out.println("\n💬 Enter symptoms (type 'done' to finish):");
                            while (true) {
                                System.out.print("Symptom: ");
                                String symptom = sc.nextLine();
                                if (symptom.equalsIgnoreCase("done")) break;
                                try {
                                    db.addSymptom(symptom);
                                } catch (UnrecognizedSymptomException e) {
                                    System.out.println("⚠️ " + e.getMessage());
                                }
                            }

                            try {
                                String[] diseases = db.diagnosis();
                                if (diseases.length == 0) {
                                    System.out.println("❗ No matching diseases found.");
                                } else {
                                    System.out.println("🧠 Possible diseases: ");
                                    for (String d : diseases) System.out.println("- " + d);
                                    db.savePatientRecord(id, name, diseases);
                                    System.out.println("💾 Record saved to medicalDatabase.csv");
                                }
                            } catch (UnrecognizedDiseaseException e) {
                                System.out.println("⚠️ " + e.getMessage());
                            }

                        } else {
                            System.out.println("❌ Login failed! Incorrect credentials.");
                        }
                    } catch (IOException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case "3":
                    System.out.println("👋 Exiting Medical Assistant. Stay healthy!");
                    return;

                default:
                    System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }
}
