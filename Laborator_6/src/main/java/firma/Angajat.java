package firma;
import java.time.LocalDate;
public class Angajat {
    private String numele;
    private String postul;
    private LocalDate dataAngajarii;
    private float salariul;


    public Angajat() {
    }


    public Angajat(String numele, String postul, LocalDate dataAngajarii, float salariul) {
        this.numele = numele;
        this.postul = postul;
        this.dataAngajarii = dataAngajarii;
        this.salariul = salariul;
    }

    // Getteri și setteri
    public String getNumele() {
        return numele;
    }

    public void setNumele(String numele) {
        this.numele = numele;
    }

    public String getPostul() {
        return postul;
    }

    public void setPostul(String postul) {
        this.postul = postul;
    }

    public LocalDate getDataAngajarii() {
        return dataAngajarii;
    }

    public void setDataAngajarii(LocalDate dataAngajarii) {
        this.dataAngajarii = dataAngajarii;
    }

    public float getSalariul() {
        return salariul;
    }

    public void setSalariul(float salariul) {
        this.salariul = salariul;
    }

    // Suprascrierea metodei toString
    @Override
    public String toString() {
        return numele + ", " + postul + ", " + dataAngajarii + ", " + salariul;
    }
}
