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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public FriendsRelations(User inviter, User receiver) {
        this.inviter = inviter;
        this.receiver = receiver;
    }
}
