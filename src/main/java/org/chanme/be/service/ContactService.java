package org.chanme.be.service;

import lombok.RequiredArgsConstructor;
import org.chanme.be.api.dto.ContactDTO;
import org.chanme.be.domain.contact.Contact;
import org.chanme.be.domain.contact.ContactRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    private Contact DTOtoEntity(ContactDTO contactDTO) {
        Contact contact = new Contact().builder()
                .nickname(contactDTO.getName())
                .phone(contactDTO.getPhone())
                .build();
        return contact;
    }

    public ContactDTO insertContact(ContactDTO contact) {
        contactRepository.save(DTOtoEntity(contact));
        return contact;
    }

    public ContactDTO updateContact(ContactDTO newContact) {
        contactRepository.save(DTOtoEntity(newContact));
        return newContact;
    }

    public void deleteContact(ContactDTO contact) {
        contactRepository.delete(DTOtoEntity(contact));
    }
}
