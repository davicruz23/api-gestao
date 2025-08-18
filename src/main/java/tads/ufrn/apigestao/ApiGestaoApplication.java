package tads.ufrn.apigestao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.enums.ProductStatus;
import tads.ufrn.apigestao.enums.UserType;
import tads.ufrn.apigestao.repository.*;
import tads.ufrn.apigestao.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiGestaoApplication {

    @Bean
    @Transactional
    public CommandLineRunner commandLineRunner(
            UserService userService,
            UserRepository userRepository,
            InspectorService inspectorService,
            InspectorRepository inspectorRepository,
            PreSaleService preSaleService,
            PreSaleRepository preSaleRepository,
            ProductService productService,
            ProductRepository productRepository,
            SaleService saleService,
            SellerRepository sellerRepository,
            ChangingService changingService,
            ChargingRepository chargingRepository,
            ChargingItemRepository chargingItemRepository

    ) {
        return args -> {
            try {
                User user = userService.findUserById(1L);
            } catch (Exception e) {
                System.out.println("começei a inserir os dados");
                List<User> users = new ArrayList<>();
                users.add(new User(null,"Marlene Balbino","12345678910","123456",UserType.SUPERADMIN, null));
                users.add(new User(null,"Miriam Balbino","12345678911","123456",UserType.FUNCIONARIO, null));
                users.add(new User(null,"Gil Bahia","12345678912","123456",UserType.FISCAL, null));
                users.add(new User(null,"José Santos","12345678913","123456",UserType.VENDEDOR, null));
                users.add(new User(null,"Carlos Miguelino","12345678914","123456",UserType.COBRADOR, null));
                userRepository.saveAll(users);

                User carregador = userService.findUserById(2L);
                User admin = userService.findUserById(1L);
                User vendedor = userService.findUserById(4L);

                List<Product> products = new ArrayList<>();
                products.add(new Product(null,"Panela Inox","Tramontina",100,50.00, ProductStatus.DISPONIVEL,admin,null));
                products.add(new Product(null,"Travesseiro","Coteminas",100,30.00, ProductStatus.DISPONIVEL,admin,null));
                products.add(new Product(null,"Ventilador","Arno",50,200.00, ProductStatus.DISPONIVEL,admin,null));
                productRepository.saveAll(products);

                List<Product> managedProducts = productRepository.findAll();


                Charging charging = new Charging();
                charging.setDescription("Carregamento do dia");
                charging.setDate(LocalDate.now());
                charging.setCreatedAt(LocalDate.now());
                charging.setUser(carregador);

                for (Product p : managedProducts) {
                    charging.addItem(p, 10);
                    p.setAmount(p.getAmount() - 10);
                }

                System.out.println("terminei de inserir os dados");

                chargingRepository.save(charging);
                productRepository.saveAll(managedProducts);

                System.out.println("Carregamento criado com sucesso!");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGestaoApplication.class, args);
    }

}
