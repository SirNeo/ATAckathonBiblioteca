package es.jmpalma.atackathon.biblioteca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.vaadin.annotations.Theme;
//import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import es.jmpalma.atackathon.biblioteca.dto.Libro;

import com.vaadin.ui.Button.ClickEvent;

import com.jcabi.aspects.Loggable;

@SpringUI(path="ui")
@Theme("valo")
public class VaadinUI extends UI {

	public static final Logger log = LoggerFactory.getLogger(VaadinUI.class.getName());
	
	@Value("${url.servicio.catalogo}")
	private String urlServicioCatalogo;
	
	@Value("${url.servicio.favoritos}")
	private String urlServicioFavoritos;

	/* (non-Javadoc)
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {

		log.debug("VaadinUI :: init :: inicio");
		RestTemplate rt = new RestTemplate();
		
		Grid<Libro> table = new Grid<>(Libro.class);
		table.setColumns("id", "nombre", "descripcion");
		table.setSizeFull();
		
		Button buttonCatalogo = new Button("Catálogo", new Button.ClickListener() {
			@Loggable
			@Override
			public void buttonClick(ClickEvent event) {
				log.debug("VaadinUI :: buttonCatalogo.buttonClick :: inicio");
				Libro[] listaLibros = rt.getForObject(urlServicioCatalogo, Libro[].class);
				ArrayList<Libro> catalogo = transforToArrayList(listaLibros);
				table.setItems(catalogo);
				
				// Vaadin 7.7.5
				//Libro[] listaLibros = rt.getForObject(urlServicioCatalogo, Libro[].class);
				//ArrayList<Libro> catalogo = transforToArrayList(listaLibros);
                //BeanItemContainer<Libro> container = new BeanItemContainer<>(Libro.class, catalogo);
				//table.setContainerDataSource(container);
				log.debug("VaadinUI :: buttonCatalogo.buttonClick :: fin");
			}
		});
		
		Button buttonFavorito = new Button("Favoritos", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	log.debug("VaadinUI :: buttonFavorito.buttonClick :: inicio");
            	Libro[] listaLibros = rt.getForObject(urlServicioFavoritos, Libro[].class);
            	ArrayList<Libro> favoritos = transforToArrayList(listaLibros);
            	table.setItems(favoritos);
            	log.debug("VaadinUI :: buttonFavorito.buttonClick :: fin");
            }
        });
		
		Layout verticalLayout = new VerticalLayout();
        Layout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(buttonCatalogo);
        horizontalLayout.addComponent(buttonFavorito);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.addComponent(table);

        setContent(verticalLayout);
        log.debug("VaadinUI :: init :: fin");
	}

	private ArrayList<Libro> transforToArrayList(Libro[] arrLibros) {

		log.debug("VaadinUI :: transforToArrayList :: inicio");
        ArrayList<Libro> arrListLibros = new ArrayList<Libro>();
        for (int i = 0; i < arrLibros.length; i++) {
            arrListLibros.add((Libro) arrLibros[i]);
        }
        log.debug("VaadinUI :: transforToArrayList :: fin");
        return arrListLibros;

    }
}
