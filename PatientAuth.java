import java.io.*;
import java.util.*;

public class PatientAuth {
    private final String patientFile = "patients.txt";

    public boolean registerPatient(String id, String name, String password) throws IOException {
        File file = new File(patientFile);
        if (!file.exists()) file.createNewFile();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 1 && parts[0].equals(id)) {
                reader.close();
                return false; // ID already exists
            }
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(id + "|" + name + "|" + password);
        writer.newLine();
        writer.close();
        return true;
    }

    public boolean authenticate(String id, String name, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(patientFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 3 && parts[0].equals(id) && parts[1].equals(name) && parts[2].equals(password)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }
}
