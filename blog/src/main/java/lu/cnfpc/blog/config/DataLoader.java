package lu.cnfpc.blog.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lu.cnfpc.blog.model.Category;
import lu.cnfpc.blog.repository.CategoryRepository;

@Component
public class DataLoader implements CommandLineRunner{

    private final CategoryRepository categoryRepository;

    public DataLoader(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Initialize Categories if no values
        if(categoryRepository.count() == 0){
            List<Category> categories = new ArrayList<>();

            Category cooking = new Category("Cooking");
            categories.add(cooking);

            Category lifestyle = new Category("Lifestyle");
            categories.add(lifestyle);

            Category work = new Category("Work");
            categories.add(work);

            categoryRepository.saveAll(categories);
        }
    }
    
}
