package firma;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.DoubleSummaryStatistics;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Încărcarea listei de angajați din fișierul JSON
        List<Angajat> angajati = loadAngajatiFromJson();

        if (angajati == null) {
            System.err.println("Nu s-a putut încărca lista de angajați.");
            return;
        }

        // 1. Afișarea listei de angajați folosind referințe la metode
        System.out.println("Lista de angajați:");
        angajati.forEach(System.out::println);

        // 2. Afișarea angajaților care au salariul peste 2500 RON
        System.out.println("\nAngajați cu salariul peste 2500 RON:");
        angajati.stream()
                .filter(angajat -> angajat.getSalariul() > 2500)
                .forEach(System.out::println);

        // 3. Crearea unei liste cu angajații din aprilie, anul trecut, cu funcție de conducere
        int anulCurent = LocalDate.now().getYear();
        List<Angajat> angajatiConducereAprilie = angajati.stream()
                .filter(angajat -> angajat.getDataAngajarii().getYear() == anulCurent - 1)
                .filter(angajat -> angajat.getDataAngajarii().getMonth() == Month.APRIL)
                .filter(angajat -> angajat.getPostul().toLowerCase().contains("sef") ||
                        angajat.getPostul().toLowerCase().contains("director"))
                .collect(Collectors.toList());

        System.out.println("\nAngajați angajați în aprilie, anul trecut, în funcții de conducere:");
        angajatiConducereAprilie.forEach(System.out::println);

        // 4. Afișarea angajaților fără funcție de conducere, în ordine descrescătoare a salariilor
        System.out.println("\nAngajați fără funcție de conducere (ordonați descrescător după salariu):");
        angajati.stream()
                .filter(angajat -> !angajat.getPostul().toLowerCase().contains("sef") &&
                        !angajat.getPostul().toLowerCase().contains("director"))
                .sorted(Comparator.comparing(Angajat::getSalariul).reversed())
                .forEach(System.out::println);

        // 5. Extragerea listei de nume de angajați în majuscule
        System.out.println("\nNumele angajaților scrise cu majuscule:");
        List<String> numeAngajatiMajuscule = angajati.stream()
                .map(angajat -> angajat.getNumele().toUpperCase())
                .collect(Collectors.toList());
        numeAngajatiMajuscule.forEach(System.out::println);

        // 6. Afișarea salariilor mai mici de 3000 RON
        System.out.println("\nSalariile mai mici de 3000 RON:");
        angajati.stream()
                .map(Angajat::getSalariul)
                .filter(salariu -> salariu < 3000)
                .forEach(System.out::println);

        // 7. Afișarea datelor primului angajat al firmei
        System.out.println("\nPrimul angajat al firmei:");
        Optional<Angajat> primulAngajat = angajati.stream()
                .min(Comparator.comparing(Angajat::getDataAngajarii));
        primulAngajat.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Nu există angajați în firmă.")
        );

        // 8. Statistici referitoare la salariul angajaților
        System.out.println("\nStatistici despre salarii:");
        DoubleSummaryStatistics statisticiSalarii = angajati.stream()
                .collect(Collectors.summarizingDouble(Angajat::getSalariul));
        System.out.printf("Salariul mediu: %.2f\n", statisticiSalarii.getAverage());
        System.out.printf("Salariul minim: %.2f\n", statisticiSalarii.getMin());
        System.out.printf("Salariul maxim: %.2f\n", statisticiSalarii.getMax());

        // 9. Verificare existență angajat cu numele „Ion”
        System.out.println("\nVerificare existență angajat cu numele „Ion”:");
        angajati.stream()
                .map(Angajat::getNumele)
                .filter(nume -> nume.contains("Ion"))
                .findAny()
                .ifPresentOrElse(
                        nume -> System.out.println("Firma are cel puțin un Ion angajat."),
                        () -> System.out.println("Firma nu are niciun Ion angajat.")
                );

        // 10. Numărul de persoane angajate în vara anului precedent
        System.out.println("\nNumărul de persoane angajate în vara anului precedent:");
        long numarAngajatiVara = angajati.stream()
                .filter(angajat -> angajat.getDataAngajarii().getYear() == anulCurent - 1)
                .filter(angajat -> {
                    Month luna = angajat.getDataAngajarii().getMonth();
                    return luna == Month.JUNE || luna == Month.JULY || luna == Month.AUGUST;
                })
                .count();
        System.out.println("Număr de angajați: " + numarAngajatiVara);
    }

    // Metodă pentru a încărca lista de angajați din fișierul JSON
    private static List<Angajat> loadAngajatiFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("angajati.json")) {
            if (input == null) {
                System.err.println("Fișierul angajati.json nu a fost găsit.");
                return null;
            }
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Angajat.class);
            return objectMapper.readValue(input, listType);
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului angajati.json: " + e.getMessage());
            return null;
        }
    }
}
