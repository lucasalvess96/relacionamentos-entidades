package com.lombok.praticas.estudos.Author;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDto getOrCreateAuthor(AuthorDto authorDto) {
        Optional<AuthorEntity> existingAuthor = authorRepository.findByName(authorDto.name());

        if (existingAuthor.isPresent()) {
            return new AuthorDto(existingAuthor.get());
        } else {
            AuthorEntity newAuthorEntity = new AuthorEntity();
            newAuthorEntity.setName(authorDto.name());
            AuthorEntity savedAuthorEntity = authorRepository.save(newAuthorEntity);
            return new AuthorDto(savedAuthorEntity);
        }
    }


}
