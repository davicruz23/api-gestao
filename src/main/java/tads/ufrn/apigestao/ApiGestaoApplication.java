package tads.ufrn.apigestao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.enums.ProductStatus;
import tads.ufrn.apigestao.enums.UserType;
import tads.ufrn.apigestao.repository.*;
import tads.ufrn.apigestao.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            ChargingItemRepository chargingItemRepository,
            ClientRepository clientRepository

    ) {
        return args -> {
            try {
                User user = userService.findUserById(1L);
            } catch (Exception e) {
                System.out.println("começei a inserir os dados");

                //salva os usuarios
                List<User> users = new ArrayList<>();
                users.add(new User(null,"Marlene Balbino","12345678910","123456",UserType.SUPERADMIN));
                users.add(new User(null,"Miriam Balbino","12345678911","123456",UserType.FUNCIONARIO));
                users.add(new User(null,"Gil Bahia","12345678912","123456",UserType.FISCAL));
                users.add(new User(null,"José Santos","12345678913","123456",UserType.VENDEDOR));
                users.add(new User(null,"Carlos Miguelino","12345678914","123456",UserType.COBRADOR));
                userRepository.saveAll(users);

                User carregador = userService.findUserById(2L);
                User admin = userService.findUserById(1L);
                User vendedor = userService.findUserById(4L);
                User inspector = userService.findUserById(3L);

                //salva os produtos
                List<Product> products = new ArrayList<>();
                products.add(new Product(null,"Panela Inox","Tramontina",100,50.00, ProductStatus.DISPONIVEL,admin,null));
                products.add(new Product(null,"Travesseiro","Coteminas",100,30.00, ProductStatus.DISPONIVEL,admin,null));
                products.add(new Product(null,"Ventilador","Arno",50,200.00, ProductStatus.DISPONIVEL,admin,null));
                productRepository.saveAll(products);

                List<Product> managedProducts = productRepository.findAll();

                //salva o carregamento com o usuario
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

                //salva o id de um vendedor
                Seller seller = new Seller();
                seller.setUser(vendedor);
                sellerRepository.save(seller);

                Inspector inspector1 = new Inspector();
                inspector1.setUser(inspector);
                inspectorRepository.save(inspector1);

                Address address1 = new Address();
                address1.setCity("Macaiba");
                address1.setState("RN");
                address1.setStreet("São José");
                address1.setNumber("270");
                address1.setZipCode("59280-676");

                Client client1 = new Client();
                client1.setName("Larissa Barbosa");
                client1.setCpf("10101010101");
                client1.setPhone("84994611450");
                client1.setAddress(address1);
                clientRepository.save(client1);

                PreSale preSale = new PreSale();
                preSale.setPreSaleDate(LocalDateTime.now());
                preSale.setSeller(seller);
                preSale.setClient(client1);
                preSale.setInspector(inspector1);
                preSale.setItems(new ArrayList<>());

                List<ChargingItem> chargingItems = charging.getItems();
                List<PreSaleItem> preSaleItems = new ArrayList<>();

                for (ChargingItem ci : chargingItems) {
                    if (ci.getProduct().getName().equals("Panela Inox") || ci.getProduct().getName().equals("Travesseiro")) {
                        PreSaleItem preItem = new PreSaleItem();
                        preItem.setProduct(ci.getProduct());
                        preItem.setPreSale(preSale);
                        preItem.setQuantity(1);
                        preSaleItems.add(preItem);

                        ci.setQuantity(ci.getQuantity() - 1);
                    }
                }

                preSale.setItems(preSaleItems);

                preSaleRepository.save(preSale);
                chargingRepository.save(charging);

                PreSale preSaleSaved = preSaleService.findById(preSale.getId());

                Sale sale = saleService.approvePreSale(
                        preSaleSaved.getId(),
                        inspector1,
                        PaymentType.CREDIT,
                        3
                );

                System.out.println("Venda aprovada pelo inspetor "
                        + preSaleSaved.getInspector().getUser().getName()
                        + " para o cliente "
                        + preSaleSaved.getClient().getName());

                System.out.println("Pré-venda criada com sucesso!");

            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGestaoApplication.class, args);
    }

}
