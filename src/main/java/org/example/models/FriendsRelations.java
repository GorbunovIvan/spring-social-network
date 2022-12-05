package org.example.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friends_relations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FriendsRelations {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
