import java.io.*;
import java.util.*;

public class MedicalDataBase {
    private List<String> symptoms = new ArrayList<>();
    private Set<String> validSymptoms;
    private Map<String, Set<String>> diseaseMap;

    public MedicalDataBase() throws IOException {
        loadSymptoms();
        loadDiseases();
    }

    private void loadSymptoms() throws IOException {
        validSymptoms = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader("symptom.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            validSymptoms.add(line.trim().toLowerCase());
        }
        br.close();
    }

    private void loadDiseases() throws IOException {
        diseaseMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("disease.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String disease = parts[0].trim();
                String[] requiredSymptoms = parts[1].split(",");
                Set<String> symSet = new HashSet<>();
                for (String s : requiredSymptoms) {
                    symSet.add(s.trim().toLowerCase());
                }
                diseaseMap.put(disease, symSet);
            }
        }
        br.close();
    }

    public void addSymptom(String symptom) throws UnrecognizedSymptomException {
        if (!validSymptoms.contains(symptom.toLowerCase())) {
            throw new UnrecognizedSymptomException("Symptom '" + symptom + "' not recognized.");
        }
        symptoms.add(symptom.toLowerCase());
    }

    public String[] diagnosis() throws UnrecognizedDiseaseException {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : diseaseMap.entrySet()) {
            if (symptoms.containsAll(entry.getValue())) {
                result.add(entry.getKey());
            }
        }
        if (result.isEmpty()) {
            throw new UnrecognizedDiseaseException("No disease matched the provided symptoms.");
        }
        return result.toArray(new String[0]);
    }

    public void savePatientRecord(String id, String name, String[] diseases) throws IOException {
        String fileName = "medicalDatabase.csv";
        if (!fileName.endsWith(".csv")) {
            throw new IOException("Invalid file type.");
        }

        File file = new File(fileName);
        boolean header = !file.exists();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        if (header) {
            writer.write("patient id,patient name,symptoms list,possible disease");
            writer.newLine();
        }

        writer.write(String.format("%s,%s,%s,%s",
                id,
                name,
                String.join(";", symptoms),
                String.join(";", diseases)
        ));
        writer.newLine();
        writer.close();
    }
}
