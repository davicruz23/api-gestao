package tads.ufrn.apigestao;

import org.springframework.security.crypto.password.PasswordEncoder;
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

//    public final PasswordEncoder passwordEncoder;
//
//    public ApiGestaoApplication(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Bean
//    @Transactional
//    public CommandLineRunner commandLineRunner(
//            UserService userService,
//            UserRepository userRepository,
//            InspectorService inspectorService,
//            InspectorRepository inspectorRepository,
//            PreSaleService preSaleService,
//            PreSaleRepository preSaleRepository,
//            ProductService productService,
//            ProductRepository productRepository,
//            SaleService saleService,
//            SellerRepository sellerRepository,
//            ChangingService changingService,
//            ChargingRepository chargingRepository,
//            ChargingItemRepository chargingItemRepository,
//            ClientRepository clientRepository,
//
//            CollectorRepository collectorRepository) {
//        return args -> {
//            try {
//                User user = userService.findUserById(1L);
//            } catch (Exception e) {
//                System.out.println("começei a inserir os dados");
//
//                //salva os usuarios
//                List<User> users = new ArrayList<>();
//                users.add(new User(null,"Marlene Balbino","1",passwordEncoder.encode("123"),UserType.SUPERADMIN));
//                users.add(new User(null,"Miriam Balbino","2",passwordEncoder.encode("123"),UserType.FUNCIONARIO));
//                users.add(new User(null,"Gil Bahia","3",passwordEncoder.encode("123"),UserType.FISCAL));
//                users.add(new User(null,"José Santos","4",passwordEncoder.encode("123"),UserType.VENDEDOR));
//                users.add(new User(null,"Carlos Miguelino","5",passwordEncoder.encode("123"),UserType.COBRADOR));
//                userRepository.saveAll(users);
//
//                User carregador = userService.findUserById(2L);
//                User admin = userService.findUserById(1L);
//                User vendedor = userService.findUserById(4L);
//                User inspector = userService.findUserById(3L);
//                User cobrador = userService.findUserById(5L);
//
//                //salva os produtos
//                List<Product> products = new ArrayList<>();
//                products.add(new Product(null,"Panela Inox","Tramontina",100,50.00, ProductStatus.DISPONIVEL,admin,null));
//                products.add(new Product(null,"Travesseiro","Coteminas",100,30.00, ProductStatus.DISPONIVEL,admin,null));
//                products.add(new Product(null,"Ventilador","Arno",50,200.00, ProductStatus.DISPONIVEL,admin,null));
//                products.add(new Product(null,"Jogo de Talheres","Tramontina",50,100.00, ProductStatus.DISPONIVEL,admin,null));
//                productRepository.saveAll(products);
//
////                List<Product> managedProducts = productRepository.findAll();
////
////                //salva o carregamento com o usuario
////                Charging charging = new Charging();
////                charging.setDescription("Carregamento do dia");
////                charging.setDate(LocalDate.now());
////                charging.setCreatedAt(LocalDate.now());
////                charging.setUser(carregador);
////
////                for (Product p : managedProducts) {
////                    charging.addItem(p, 10);
////                    p.setAmount(p.getAmount() - 10);
////                }
////
////                System.out.println("terminei de inserir os dados");
////
////                chargingRepository.save(charging);
////                productRepository.saveAll(managedProducts);
//
//                //salva o id de um vendedor
//                Seller seller = new Seller();
//                seller.setUser(vendedor);
//                sellerRepository.save(seller);
//
//                Inspector inspector1 = new Inspector();
//                inspector1.setUser(inspector);
//                inspectorRepository.save(inspector1);
//
//                Collector collector1 = new Collector();
//                collector1.setUser(cobrador);
//                collectorRepository.save(collector1);
//
//                /*List<Client> clients = List.of(
//                        new Client(null,"Larissa Barbosa","10101010101","84994611450",
//                                new Address(null,"Macaiba","RN","São José","57","59280-646",null)
//                        ),
//                        new Client(null,"Elizabete Cruz de Souza","11111111111","84992049249",
//                                new Address(null,"Macaiba","RN","Maria Hilda Costa Torres","270","59280-676",null)
//                        )
//                );
//
//                clientRepository.saveAll(clients);
//
//
//                List<Client> savedClients = clientRepository.findAll();
//
//                PreSale preSale1 = new PreSale();
//                preSale1.setPreSaleDate(LocalDateTime.now());
//                preSale1.setSeller(seller);
//                preSale1.setClient(savedClients.get(0)); // Larissa Barbosa
//                preSale1.setInspector(inspector1);
//                preSale1.setItems(new ArrayList<>());
//
//                List<PreSaleItem> preSaleItems1 = new ArrayList<>();
//                for (ChargingItem ci : charging.getItems()) {
//                    if (ci.getProduct().getName().equals("Panela Inox") || ci.getProduct().getName().equals("Travesseiro") || ci.getProduct().getName().equals("Jogo de Talheres"))  {
//                        PreSaleItem preItem = new PreSaleItem();
//                        preItem.setProduct(ci.getProduct());
//                        preItem.setPreSale(preSale1);
//                        preItem.setQuantity(1);
//                        preItem.setChargingItem(ci);
//                        preSaleItems1.add(preItem);
//                        ci.setQuantity(ci.getQuantity() - 1);
//                    }
//                }
//                preSale1.setItems(preSaleItems1);
//                preSaleRepository.save(preSale1);
//
//                PreSale preSale2 = new PreSale();
//                preSale2.setPreSaleDate(LocalDateTime.now());
//                preSale2.setSeller(seller);
//                preSale2.setClient(savedClients.get(1));
//                preSale2.setInspector(inspector1);
//                preSale2.setItems(new ArrayList<>());
//
//                List<PreSaleItem> preSaleItems2 = new ArrayList<>();
//                for (ChargingItem ci : charging.getItems()) {
//                    if (ci.getProduct().getName().equals("Travesseiro") || ci.getProduct().getName().equals("Ventilador")) {
//                        PreSaleItem preItem = new PreSaleItem();
//                        preItem.setProduct(ci.getProduct());
//                        preItem.setPreSale(preSale2);
//                        preItem.setQuantity(1);
//                        preItem.setChargingItem(ci);
//                        preSaleItems2.add(preItem);
//                        ci.setQuantity(ci.getQuantity() - 1);
//                    }
//                }
//                preSale2.setItems(preSaleItems2);
//                preSaleRepository.save(preSale2);
//
//                chargingRepository.save(charging);
//
//
//
//                PreSale preSaleSaved1 = preSaleService.findById(preSale1.getId());
//
//                saleService.approvePreSale(
//                        preSaleSaved1.getId(),
//                        inspector1,
//                        PaymentType.CREDIT,
//                        3
//                );
//
//                System.out.println("Venda aprovada pelo fiscal "
//                        + preSaleSaved1.getInspector().getUser().getName()
//                        + " para o cliente "
//                        + preSaleSaved1.getClient().getName());
//
//                PreSale preSaleSaved2 = preSaleService.findById(preSale2.getId());
//
//                saleService.rejectPreSale(
//                        preSaleSaved2.getId(),
//                        inspector1
//                );
//
//                System.out.println("Venda recusada pelo fiscal "
//                        + preSaleSaved2.getInspector().getUser().getName()
//                        + " para o cliente "
//                        + preSaleSaved2.getClient().getName());
//
//                System.out.println("Pré-venda criada com sucesso!");*/
//
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(ApiGestaoApplication.class, args);
    }

}
