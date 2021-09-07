
package ml.kalanblowSystemManagement.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Table(
        name = "device")
@Entity
@Getter
@Setter
@Accessors(
        chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String deviceDetails;
    private String location;
    private LocalDateTime lastLoggedIn;

}
