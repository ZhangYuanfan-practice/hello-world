package com.example.labmvc.controller;

import com.example.labmvc.domain.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@RestController                 // 相当于 @Controller + @ResponseBody
@RequestMapping("/api/users")   // 类级别映射
public class UserController {

    // 模拟内存数据源
    private final Map<Long, User> repo = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    // 1. GET 集合
    @GetMapping
    public List<User> all() {
        return new ArrayList<>(repo.values());
    }

    // 2. GET 单条
    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return repo.get(id);
    }

    // 3. POST 新增
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        long id = counter.getAndIncrement();
        user.setId(id);
        repo.put(id, user);
        return user;
    }

    // 4. PUT 全量更新
    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @Valid @RequestBody User user) {
        user.setId(id);
        repo.put(id, user);
        return user;
    }

    // 5. DELETE 删除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.remove(id);
    }

    // 6. 带查询参数的分页搜索
    @GetMapping("/search")
    public List<User> search(@RequestParam(required = false) String name,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Stream<User> stream = repo.values().stream();
        if (name != null) {
            stream = stream.filter(u -> u.getName().contains(name));
        }
        return stream.skip((long) page * size).limit(size).toList();
    }
}
