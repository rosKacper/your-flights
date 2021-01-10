package pl.edu.agh.ki.lab.to.yourflights.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.Reservation;
import pl.edu.agh.ki.lab.to.yourflights.repository.ReservationRepository;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailHandler {

    private ReservationService reservationService;
    public EmailHandler(ReservationService reservationService){
        this.reservationService=reservationService;

    }
    public void sendEmail(CustomerService customerService, Flight flight, Reservation reservation) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable task = () -> {
            String from = "trywialne.pomijamy@gmail.com";
            String password = "admin1615";
            String host = "smtp.gmail.com";
            String port = "587";
            Properties properties = new Properties();
            // Setup mail server
            properties.put("mail.transport.protocol", "smtps");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                List<Customer> customerList = customerService.findByUsername(username);
                // Set From: header field of the header.
                String to = customerList.get(0).getEmailAddress();
                message.setFrom(from);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Reservation");
                message.setText("You have created reservation for flight from " + flight.getPlaceOfDeparture() + " to " + flight.getPlaceOfDestination() + " leaving at ");
                reservation.setStatus("Created - informed");
                Transport.send(message);

            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        };

        if (reservation.getStatus().equals("Created"))
        {
            executorService.submit(task);
        }

        executorService.shutdown();

    }

    public void upcomingEmail(){
        List<Reservation> reservations=reservationService.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals("Created - informed")) {
                Flight flightTmp = reservation.getTicketOrders().get(0).getTicketCategory().getFlight();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate departureDate = LocalDate.parse(flightTmp.getDepartureDate(), formatter);
                LocalDate localDate = LocalDate.now();
                LocalTime localTime = LocalTime.now();
                LocalTime departureTime = LocalTime.parse(flightTmp.getDepartureTime());
                if(departureDate.getYear()==localDate.getYear() && departureDate.getMonth()==localDate.getMonth()
                && (departureDate.getDayOfMonth()==localDate.getDayOfMonth() ||
                        (departureDate.getDayOfMonth()==localDate.getDayOfMonth()+1 && departureTime.getHour()<localTime.getHour())))
                {
                    int a=1;
                }
            }
            int x=1;
        }
        int x=2;
    }
}

