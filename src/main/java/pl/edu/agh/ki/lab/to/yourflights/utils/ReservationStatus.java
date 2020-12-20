package pl.edu.agh.ki.lab.to.yourflights.utils;

/**
 * Enumerator definiujący status rezerwacji
 */
public enum ReservationStatus {
    ACTIVE("ACTIVE"),
    CANCELLED("CANCELLED");

    public final String status;

    private ReservationStatus(String role) {
        this.status = role;
    }
}
