package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "name", "birthDay"})
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    private String login;
    private String password;

    @OneToMany(mappedBy = "sender", cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<Message> messagesSent = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<Message> messagesReceived = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "inviter", cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<FriendsRelations> friendsInvited = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<FriendsRelations> friendsInviters = new ArrayList<>();

    public User(String name, LocalDate birthDay) {
        this.name = name;
        this.birthDay = birthDay;
        this.login = name;
        this.password = "";
    }

    /** for tests */
    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.login = name;
        this.password = "";
    }

    public List<User> getFriends() {

        List<User> friends = new ArrayList<>();

        // those who invited this user
        friends.addAll(friendsInviters.stream()
                                .map(FriendsRelations::getInviter)
                                .toList());

        // those who were invited by this user
        friends.addAll(friendsInvited.stream()
                                .map(FriendsRelations::getReceiver)
                                .toList());

        return friends;
    }

    public void sendMessage(User receiver, String text) {
        Message message = new Message(this, receiver, text);
        messagesSent.add(message);
    }

    public void addPost(String text) {
        Post post = new Post(this, text);
        posts.add(post);
    }

    public FriendsRelations addFriend(User friend) {
        FriendsRelations friendsRelations = new FriendsRelations(this, friend);
        friendsInvited.add(friendsRelations);
        return friendsRelations;
    }
}
