package dn.mp_warehouse.domain.service;

import dn.mp_warehouse.api.dto.ProductFilterDto;
import dn.mp_warehouse.api.dto.ProductInputDto;
import dn.mp_warehouse.api.dto.ProductOutDto;
import dn.mp_warehouse.domain.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<ProductEntity> withSort(ProductFilterDto productFilterDto);

    ProductOutDto findById(String id);

    ProductOutDto findByName(String name);

    ProductOutDto save(ProductInputDto productInputDto);

    void update(String id, ProductInputDto productInputDto);

    void deleteById(String id);

    void deleteAll(List<String> ids);

    Long getCount(String id);


}
