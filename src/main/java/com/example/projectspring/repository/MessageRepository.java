package com.example.projectspring.repository;

import com.example.projectspring.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(Long senderId1, Long receiverId1, Long senderId2, Long receiverId2);

	    // Récupérer tous les messages entre deux utilisateurs triés par date
	    List<Message> findBySenderIdAndReceiverIdOrderBySentAtAsc(Long senderId, Long receiverId);

	    // Récupérer tous les messages où l'utilisateur est soit l'expéditeur, soit le destinataire
	    List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
	}