package healthcare.FHIRService;

import healthcare.Alert.AlertEntity;
import healthcare.Entity.NotificationEntity;
import healthcare.Repository.NotificationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendAlertNotification(AlertEntity alert) {

        if (alert == null) {
            return;
        }

        // Decide recipient based on alert severity
        String recipient;

        if ("CRITICAL".equalsIgnoreCase(alert.getSeverity())) {
            recipient = "DOCTOR";
        } else if ("HIGH".equalsIgnoreCase(alert.getSeverity())) {
            recipient = "NURSE";
        } else {
            recipient = "ROUTINE_MONITORING";
        }

        // Create notification
        NotificationEntity notification = new NotificationEntity();

        notification.setPatientId(alert.getPatientId());
        notification.setAlertId(alert.getId());
        notification.setAlertType(alert.getAlertType());
        notification.setMessage(alert.getMessage());
        notification.setSeverity(alert.getSeverity());

        notification.setRecipient(recipient);
        notification.setStatus("UNREAD");
        notification.setCreatedAt(LocalDateTime.now());

        // Save notification in MongoDB
        NotificationEntity savedNotification =
                notificationRepo.save(notification);

        // Send notification through WebSocket
        messagingTemplate.convertAndSend(
                "/topic/alerts",
                savedNotification
        );

        System.out.println("=================================");
        System.out.println("NOTIFICATION CREATED");
        System.out.println("Patient: " + alert.getPatientId());
        System.out.println("Alert: " + alert.getAlertType());
        System.out.println("Recipient: " + recipient);
        System.out.println("WebSocket notification sent");
        System.out.println("=================================");
    }
}

