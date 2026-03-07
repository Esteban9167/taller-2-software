/*
 * Copyright (C) 2015 cesarvefe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.unisabana.dyas.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.sql.SQLException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.unisabana.dyas.samples.entities.Cliente;
import edu.unisabana.dyas.samples.entities.Item;
import edu.unisabana.dyas.samples.entities.TipoItem;

/**
 *
 * @author cesarvefe
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();

        SqlSession sqlss = sessionfact.openSession();

        // Crear el mapper y usarlo: 
        ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);

        System.out.println("Clientes antes de agregar:");
        List<Cliente> clientes = cm.consultarClientes();
        for (Cliente c : clientes) {
            System.out.println(c);
        }

        // Agregar un item rentado a un cliente
        cm.agregarItemRentadoACliente(123456789, 2, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis() + 86400000L)); // Agregar item 2 al cliente 123456789

        System.out.println("\nClientes después de agregar:");
        clientes = cm.consultarClientes();
        for (Cliente c : clientes) {
            System.out.println(c);
        }

        // Insertar un nuevo item
        ItemMapper im = sqlss.getMapper(ItemMapper.class);
        TipoItem tipo = new TipoItem(1, "Electrónico");
        Item nuevoItem = new Item(tipo, 4, "Nuevo Item", "Descripción del nuevo item", new java.sql.Date(System.currentTimeMillis()), 3000, "Diario", "Nuevo Género");
        im.insertarItem(nuevoItem);

        System.out.println("\nItems después de insertar:");
        List<Item> items = im.consultarItems();
        for (Item i : items) {
            System.out.println(i);
        }

        System.out.println("\nConsultar item específico (id=1):");
        Item itemEspecifico = im.consultarItem(1);
        System.out.println(itemEspecifico);
        

        sqlss.commit();
        sqlss.close();

    }


}
