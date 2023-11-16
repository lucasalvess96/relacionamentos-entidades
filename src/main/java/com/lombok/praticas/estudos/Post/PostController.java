package com.lombok.praticas.estudos.Post;

import com.lombok.praticas.estudos.Post.Dto.PostDto;
import com.lombok.praticas.estudos.Post.Dto.PostSearchDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<PostDto> create(@RequestBody @Valid PostDto postDto) {
        PostDto postCreate = postService.postCreate(postDto);
        return ResponseEntity.created(URI.create("/create" + postDto.id()))
                .body(postCreate);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PostDto>> patientList() {
        return ResponseEntity.ok()
                .body(postService.postList());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<PostDto>> patientPagination(@PageableDefault(direction = Sort.Direction.DESC)
                                                           Pageable pageable) {
        return ResponseEntity.ok()
                .body(postService.postPagination(pageable));
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postUpdateDto) {
        PostDto updatedPost = postService.updatePost(postId, postUpdateDto);
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PostDto> detail(@PathVariable @Valid Long id) {
        Optional<PostDto> postDetailDto = postService.patientDetail(id);
        return postDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<PostSearchDto>> pagedSearch(@RequestParam String title, Pageable pageable) {
        Page<PostSearchDto> postSearchDtos = postService.searchPostPagination(title, pageable);
        return ResponseEntity.ok()
                .body(postSearchDtos);
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<PostSearchDto>> searchList(@RequestParam String title) {
        List<PostSearchDto> postSearchDtos = postService.searchPostList(title);
        return ResponseEntity.ok()
                .body(postSearchDtos);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent()
                .build();
    }
}
