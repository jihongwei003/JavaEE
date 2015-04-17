/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Files;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JiHongwei
 */
@Stateless
public class FilesFacade extends AbstractFacade<Files> {
    @PersistenceContext(unitName = "GCGJAppPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FilesFacade() {
        super(Files.class);
    }
    
}
