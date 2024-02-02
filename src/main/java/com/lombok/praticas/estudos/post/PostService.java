package com.lombok.praticas.estudos.post;

import com.lombok.praticas.estudos.author.AuthorEntity;
import com.lombok.praticas.estudos.author.AuthorRepository;
import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.post.dto.PostDto;
import com.lombok.praticas.estudos.post.dto.PostSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public PostDto postCreate(PostDto postDto) {
        Optional<AuthorEntity> existingAuthor = authorRepository.findByName(postDto.authorDto().name());
        AuthorEntity authorEntity;
        if (existingAuthor.isPresent()) {
            authorEntity = existingAuthor.get();
        } else {
            authorEntity = new AuthorEntity();
            authorEntity.setName(postDto.authorDto().name());
            authorEntity = authorRepository.save(authorEntity);
        }
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDto.title());
        postEntity.setAuthorEntity(authorEntity);
        return new PostDto(postRepository.save(postEntity));
    }

    public List<PostDto> postList() {
        List<PostEntity> patientEntityList = postRepository.findAll();
        return patientEntityList.stream().map(PostDto::new).toList();
    }

    public Page<PostDto> postPagination(Pageable pageable) {
        Page<PostEntity> patientEntities = postRepository.findAll(pageable);
        return patientEntities.map(PostDto::new);
    }

    public PostDto updatePost(Long postId, PostDto postUpdateDto) {
        PostEntity existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ErroRequest("usuário não encontrado"));
        if (postUpdateDto.title() != null && !postUpdateDto.title().isEmpty()) {
            existingPost.setTitle(postUpdateDto.title());
        }
        if (postUpdateDto.authorDto().name() != null && !postUpdateDto.authorDto().name().isEmpty()) {
            existingPost.getAuthorEntity().setName(postUpdateDto.authorDto().name());
        }
        PostEntity updatedPost = postRepository.save(existingPost);
        return new PostDto(updatedPost);
    }

    public Optional<PostDto> patientDetail(Long id) {
        Optional<PostEntity> posttEntity = postRepository.findById(id);
        return posttEntity.map(entity -> Optional.of(new PostDto(entity)))
                .orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
    }

    public Page<PostSearchDto> searchPostPagination(String title, Pageable pageable) {
        Page<PostEntity> postEntityPage = postRepository.findByTitleContainingIgnoreCase(title, pageable);
        return postEntityPage.map(postEntity -> new PostSearchDto(postEntity.getTitle()));
    }

    public List<PostSearchDto> searchPostList(String title) {
        List<PostEntity> postEntityList = postRepository.findByTitleContainingIgnoreCase(title);
        return postEntityList.stream().map(postEntity -> new PostSearchDto(postEntity.getTitle())).toList();
    }

    public void deletePost(Long postId) {
        PostEntity postToDelete = postRepository.findById(postId)
                .orElseThrow(() -> new ErroRequest("usuário não encontrado"));
        AuthorEntity author = postToDelete.getAuthorEntity();
        if (author.getPostEntityList().size() == 1) {
            authorRepository.delete(author);
        }
        postRepository.delete(postToDelete);
    }
}
