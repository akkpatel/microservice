package com.atdms.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.atdms.app.domain.Sku;

import com.atdms.app.repository.SkuRepository;
import com.atdms.app.web.rest.errors.BadRequestAlertException;
import com.atdms.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.io.InputStream;
import java.util.List;
import org.json.*;
// import org.json.simple.JSONArray;


/**
 * REST controller for managing Sku.
 */
@RestController
@RequestMapping("/api")
public class SkuResource {

    private final Logger log = LoggerFactory.getLogger(SkuResource.class);

    private static final String ENTITY_NAME = "sku";

    private final SkuRepository skuRepository;

    public SkuResource(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }

    /**
     * POST  /skus : Create a new sku.
     *
     * @param sku the sku to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sku, or with status 400 (Bad Request) if the sku has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skus")
    @Timed
    public ResponseEntity<Sku> createSku(@RequestBody Sku sku) throws URISyntaxException {
        log.debug("REST request to save Sku : {}", sku);
        if (sku.getId() != null) {
            throw new BadRequestAlertException("A new sku cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sku result = skuRepository.save(sku);
        return ResponseEntity.created(new URI("/api/skus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skus : Updates an existing sku.
     *
     * @param sku the sku to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sku,
     * or with status 400 (Bad Request) if the sku is not valid,
     * or with status 500 (Internal Server Error) if the sku couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skus")
    @Timed
    public ResponseEntity<Sku> updateSku(@RequestBody Sku sku) throws URISyntaxException {
        log.debug("REST request to update Sku : {}", sku);
        if (sku.getId() == null) {
            return createSku(sku);
        }
        Sku result = skuRepository.save(sku);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sku.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skus : get all the skus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of skus in body
     */
    @GetMapping("/skus")
    @Timed
    public List<Sku> getAllSkus() {
        log.debug("REST request to get all Skus");
        return skuRepository.findAll();
        }


    /**
     * GET  /skus/sample : sends all the sample data.
     *
     * @return the ResponseEntity with status 200 (OK) and the sample json in body
     */
    @GetMapping("/skus/sample")
    @Timed
    public String getSampleSkus() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj = new JSONObject("{ \"sales\": { \"yearToDate\": 2018214.36, \"previousYearToDate\": 1902616.22, \"change\": 6, \"data\": [ ] }, \"volume\": { \"yearToDate\": 2018214.36, \"previousYearToDate\": 1902616.22, \"change\": 6, \"data\": [ ] }, \"brandOverview\": [ { \"style\": { \"name\": \"style1\", \"sku\": \"sku\", \"ytd\": { \"quantity\": 1, \"currentSales\": 1, \"previousSales\": 1, \"percentChange\": 1 }, \"qtd\": { \"quantity\": 1, \"currentSales\": 1, \"previousSales\": 1, \"percentChange\": 1 }, \"mtd\": { \"quantity\": 1, \"currentSales\": 1, \"previousSales\": 1, \"percentChange\": 1 } }, \"brand\": { \"name\": \"brand name\", \"quantity\": 1, \"ytd\": { \"quantity\": 1, \"currentSales\": 1, \"previousSales\": 1, \"percentChange\": 1 }, \"qtd\": { \"quantity\": 1, \"currentSales\": 1, \"previousSales\": 1, \"percentChange\": 1 }, \"mtd\": { \"quantity\": 1, \"currentSales\": 1, \"previousSales\": 1, \"percentChange\": 1 } } } ], \"brands\": [ { \"name\": \"brand name\", \"category\": \"category\" } ] } ");
            // jsonObj.put("aa", "bb");
            // System.out.println(jsonObj);
        } catch (JSONException e) {
            System.out.println(e);
        }  
        return jsonObj.toString();
    }

    /**
     * GET  /skus/:id : get the "id" sku.
     *
     * @param id the id of the sku to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sku, or with status 404 (Not Found)
     */
    @GetMapping("/skus/{id}")
    @Timed
    public ResponseEntity<Sku> getSku(@PathVariable Long id) {
        log.debug("REST request to get Sku : {}", id);
        Sku sku = skuRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sku));
    }

    /**
     * DELETE  /skus/:id : delete the "id" sku.
     *
     * @param id the id of the sku to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skus/{id}")
    @Timed
    public ResponseEntity<Void> deleteSku(@PathVariable Long id) {
        log.debug("REST request to delete Sku : {}", id);
        skuRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
