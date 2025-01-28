package com.lombok.praticas.estudos.manytoone.post;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.manytoone.author.AuthorService;
import com.lombok.praticas.estudos.manytoone.post.dto.PostDto;
import com.lombok.praticas.estudos.manytoone.post.dto.PostSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final AuthorService authorService;

    public PostService(PostRepository postRepository, AuthorService authorService) {
        this.postRepository = postRepository;
        this.authorService = authorService;
    }

    public PostDto postCreate(PostDto postDto) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDto.title());
        postEntity.setAuthorEntity(authorService.saveAuthor(postDto.authorDto()));
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
        PostEntity existingPost = postRepository.findById(postId).orElseThrow(() -> new ErroRequest("usuário não encontrado"));
        existingPost.setTitle(postUpdateDto.title());
        existingPost.getAuthorEntity().setName(postUpdateDto.authorDto().name());
        return new PostDto(postRepository.save(existingPost));
    }

    public Optional<PostDto> patientDetail(Long id) {
        Optional<PostEntity> posttEntity = postRepository.findById(id);
        return posttEntity.map(entity -> Optional.of(new PostDto(entity))).orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
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
        PostEntity postToDelete = postRepository.findById(postId).orElseThrow(() -> new ErroRequest("usuário não encontrado"));
        postRepository.delete(postToDelete);
    }
}
