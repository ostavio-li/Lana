package com.edwardlee.library.service;

/**
 * @author EdwardLee
 * @date 2020/11/2
 */

import com.edwardlee.library.util.OntOperation;
import com.edwardlee.library.util.RegOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "ontologyService")
public class OntologyService {

    private OntOperation operation;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void setOperation(OntOperation operation) {
        this.operation = operation;
    }

    public String search(String question) {
        if (RegOperation.match(operation.REG_AUTHOR_BY_BOOK, question)) {
            String author = RegOperation.getParam(1);
            logger.info("Author:\t" + author);

            return author;
        }
        return question;
    }

    public String findBookByAuthor(String author) {
        String s = operation.getPropertyReader().readProperty("query-author-book");
        return s;
    }

}
