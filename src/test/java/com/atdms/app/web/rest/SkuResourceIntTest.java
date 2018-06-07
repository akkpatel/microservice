package com.atdms.app.web.rest;

import com.atdms.app.AtdmsApp;

import com.atdms.app.domain.Sku;
import com.atdms.app.repository.SkuRepository;
import com.atdms.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.atdms.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SkuResource REST controller.
 *
 * @see SkuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtdmsApp.class)
public class SkuResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_CURRENT_SALES = 1;
    private static final Integer UPDATED_CURRENT_SALES = 2;

    private static final Integer DEFAULT_PREVIOUS_SALES = 1;
    private static final Integer UPDATED_PREVIOUS_SALES = 2;

    private static final Integer DEFAULT_PERCENT_CHANGE = 1;
    private static final Integer UPDATED_PERCENT_CHANGE = 2;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkuMockMvc;

    private Sku sku;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkuResource skuResource = new SkuResource(skuRepository);
        this.restSkuMockMvc = MockMvcBuilders.standaloneSetup(skuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sku createEntity(EntityManager em) {
        Sku sku = new Sku()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .currentSales(DEFAULT_CURRENT_SALES)
            .previousSales(DEFAULT_PREVIOUS_SALES)
            .percentChange(DEFAULT_PERCENT_CHANGE);
        return sku;
    }

    @Before
    public void initTest() {
        sku = createEntity(em);
    }

    @Test
    @Transactional
    public void createSku() throws Exception {
        int databaseSizeBeforeCreate = skuRepository.findAll().size();

        // Create the Sku
        restSkuMockMvc.perform(post("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sku)))
            .andExpect(status().isCreated());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeCreate + 1);
        Sku testSku = skuList.get(skuList.size() - 1);
        assertThat(testSku.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSku.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSku.getCurrentSales()).isEqualTo(DEFAULT_CURRENT_SALES);
        assertThat(testSku.getPreviousSales()).isEqualTo(DEFAULT_PREVIOUS_SALES);
        assertThat(testSku.getPercentChange()).isEqualTo(DEFAULT_PERCENT_CHANGE);
    }

    @Test
    @Transactional
    public void createSkuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skuRepository.findAll().size();

        // Create the Sku with an existing ID
        sku.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkuMockMvc.perform(post("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sku)))
            .andExpect(status().isBadRequest());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSkus() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);

        // Get all the skuList
        restSkuMockMvc.perform(get("/api/skus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sku.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].currentSales").value(hasItem(DEFAULT_CURRENT_SALES)))
            .andExpect(jsonPath("$.[*].previousSales").value(hasItem(DEFAULT_PREVIOUS_SALES)))
            .andExpect(jsonPath("$.[*].percentChange").value(hasItem(DEFAULT_PERCENT_CHANGE)));
    }

    @Test
    @Transactional
    public void getSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);

        // Get the sku
        restSkuMockMvc.perform(get("/api/skus/{id}", sku.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sku.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.currentSales").value(DEFAULT_CURRENT_SALES))
            .andExpect(jsonPath("$.previousSales").value(DEFAULT_PREVIOUS_SALES))
            .andExpect(jsonPath("$.percentChange").value(DEFAULT_PERCENT_CHANGE));
    }

    @Test
    @Transactional
    public void getNonExistingSku() throws Exception {
        // Get the sku
        restSkuMockMvc.perform(get("/api/skus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);
        int databaseSizeBeforeUpdate = skuRepository.findAll().size();

        // Update the sku
        Sku updatedSku = skuRepository.findOne(sku.getId());
        // Disconnect from session so that the updates on updatedSku are not directly saved in db
        em.detach(updatedSku);
        updatedSku
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .currentSales(UPDATED_CURRENT_SALES)
            .previousSales(UPDATED_PREVIOUS_SALES)
            .percentChange(UPDATED_PERCENT_CHANGE);

        restSkuMockMvc.perform(put("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSku)))
            .andExpect(status().isOk());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeUpdate);
        Sku testSku = skuList.get(skuList.size() - 1);
        assertThat(testSku.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSku.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSku.getCurrentSales()).isEqualTo(UPDATED_CURRENT_SALES);
        assertThat(testSku.getPreviousSales()).isEqualTo(UPDATED_PREVIOUS_SALES);
        assertThat(testSku.getPercentChange()).isEqualTo(UPDATED_PERCENT_CHANGE);
    }

    @Test
    @Transactional
    public void updateNonExistingSku() throws Exception {
        int databaseSizeBeforeUpdate = skuRepository.findAll().size();

        // Create the Sku

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkuMockMvc.perform(put("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sku)))
            .andExpect(status().isCreated());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);
        int databaseSizeBeforeDelete = skuRepository.findAll().size();

        // Get the sku
        restSkuMockMvc.perform(delete("/api/skus/{id}", sku.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sku.class);
        Sku sku1 = new Sku();
        sku1.setId(1L);
        Sku sku2 = new Sku();
        sku2.setId(sku1.getId());
        assertThat(sku1).isEqualTo(sku2);
        sku2.setId(2L);
        assertThat(sku1).isNotEqualTo(sku2);
        sku1.setId(null);
        assertThat(sku1).isNotEqualTo(sku2);
    }
}
