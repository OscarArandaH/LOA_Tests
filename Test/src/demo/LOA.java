package demo;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.Duration;

public class LOA {
	@Test
	public void Inscribir() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		// Seleccionar proceso de Inscripcion
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Postulación 1/2023")).click();
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame("derecho");
		// Se obtienen todas las asignaturas
		List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector("tr"));
		// Se almacenan todas las asignaturas
		ArrayList<String> listadoAsignaturasAux = new ArrayList<String>();
		for (WebElement e : listadoAsignaturas) {
			// Se ignora el encabezado de la lista
			if( e.getText().split(" ")[2].compareTo("N") != 0  ){
				String[] asignatura = e.getText().split(" ");
				String tmp = "";
				for( int i = 1; i < asignatura.length - 1 ; i++ ) {
					tmp += " " + asignatura[i];
				}
				listadoAsignaturasAux.add(tmp.strip());
			}
		}
		// Se revisan todos los cursos
		int i = 0;
		while( i == 0) {
			// Seleccionar frame del listado de cursos
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("derecho");
			Boolean existeTeoria = false;
			Boolean existeLaboratorio = false;
			Boolean existeEjercicio = false;
			Boolean seleccionoTeoria = false;
			Boolean seleccionoLaboratorio = false;
			Boolean seleccionoEjercicio = false;
			// Se crea el random para seleccionar una asignatura al azar
			Random rand = new Random();
			int rand_int1 = rand.nextInt(listadoAsignaturasAux.size());
			// Se selecciona una asignatura
			driver.findElement(By.linkText(listadoAsignaturasAux.get(rand_int1))).click();
			System.out.println("\nAsignatura: " + listadoAsignaturasAux.get(rand_int1));
			// Se sale del frame, y se entra al frame de los cursos de teoria.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_cteo");
			// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
			driver.switchTo().frame("mainFrame");
			System.out.println("	Se revisan los cursos de teoria");
			// Se obtienen todos los cursos
			List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
			// Se almacenan todas las secciones
			ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
			for (WebElement e : listadoTeoria) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals("No hay coordinaciones definidas.") ){
					// No hay cursos de teoria
					System.out.println("		No hay cursos de teoria");
				} else {
					listadoTeoriaAux.add(tmp);
					existeTeoria = true;
				}
			}
			// Se prueba seleccionar una seccion de teoria
			for ( int j = 0; j < listadoTeoriaAux.size(); j++ ) {
				String seccion = listadoTeoriaAux.get(j).replace("\n", " "	).strip();
				String[] seccionAux = seccion.split(" ");
				int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
				System.out.println("		Seccion " + (j+1) + " | " + seccion);
				System.out.println("		Seccion " + (j+1) + " | Cupos: " + cupos);
				// Se revisa si existe cupo
				if( cupos > 0 ){
					// Si hay cupos, se selecciona
					driver.findElement(By.cssSelector(".row-curso:nth-child("+(j+1)+") > td:nth-child(1)")).click();
					seleccionoTeoria = true;
					System.out.println("		Seccion " + (j+1) + " | Seleccionada");
					break;
				} else {
					// Si no hay cupos, se selecciona el siguiente curso
					System.out.println("		Seccion " + (j+1) + " | Sin cupos");
				}
			}
			// Se sale del frame, y se entra al frame de los cursos de laboratorio.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_clab");
			// Se selecciona el frame de los cursos (::LISTADO LABORATORIOS es un frame)
			driver.switchTo().frame("mainFrame");
			System.out.println("	Se revisan los cursos de laboratorio");
			// Se obtienen todos los cursos
			List<WebElement> listadoLaboratorio = driver.findElements(By.cssSelector("tr"));
			// Se almacenan todas las secciones
			ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
			for (WebElement e : listadoLaboratorio) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals("No hay coordinaciones de laboratorio definidas.") ){
					// No hay cursos de laboratorio
					System.out.println("		No hay cursos de laboratorio");
				} else {
					listadoLaboratorioAux.add(tmp);
					existeLaboratorio = true;
				}
			}
			// Se prueba seleccionar una seccion de laboratorio
			for ( int j = 0; j < listadoLaboratorioAux.size(); j++ ) {
				String seccion = listadoLaboratorioAux.get(j).replace("\n", " "	).strip();
				String[] seccionAux = seccion.split(" ");
				int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
				System.out.println("		Seccion " + (j+1) + " | " + seccion);
				System.out.println("		Seccion " + (j+1) + " | Cupos: " + cupos);
				// Se revisa si existe cupo
				if( cupos > 0 ){
					// Si hay cupos, se selecciona
					driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(j+1)+") > td:nth-child(1)")).click();
					seleccionoLaboratorio = true;
					System.out.println("		Seccion " + (j+1) + " | Seleccionada");
					break;
				} else {
					// Si no hay cupos, se selecciona el siguiente curso
					System.out.println("		Seccion " + (j+1) + " | Sin cupos");
				}
			}
			// Se sale del frame, y se entra al frame de los cursos de ejercicio.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_ceje");
			// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
			driver.switchTo().frame("mainFrame");
			System.out.println("	Se revisan los cursos de ejercicio");
			// Se obtienen todos los cursos
			List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
			// Se almacenan todas las secciones
			ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
			for (WebElement e : listadoEjercicio) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals("No hay coordinaciones de ejercicios definidas.") ){
					// No hay cursos de ejercicios
					System.out.println("		No hay cursos de ejercicios");
				} else {
					listadoEjercicioAux.add(tmp);
					existeEjercicio = true;
				}
			}
			// Se prueba seleccionar una seccion de ejercicio
			for ( int j = 0; j < listadoEjercicioAux.size(); j++ ) {
				String seccion = listadoEjercicioAux.get(j).replace("\n", " "	).strip();
				String[] seccionAux = seccion.split(" ");
				int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
				System.out.println("		Seccion " + (j+1) + " | " + seccion);
				System.out.println("		Seccion " + (j+1) + " | Cupos: " + cupos);
				// Se revisa si existe cupo
				if( cupos > 0 ){
					// Si hay cupos, se selecciona
					driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(j+1)+") > td:nth-child(1)")).click();
					seleccionoEjercicio = true;
					System.out.println("		Seccion " + (j+1) + " | Seleccionada");
					break;
				} else {
					// Si no hay cupos, se selecciona el siguiente curso
					System.out.println("		Seccion " + (j+1) + " | Sin cupos");
				}
			}
			// Se revisa que exista al menos un curso
			if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
				// Se puede postular a la asignatura
				System.out.println("	Se puede postular a la asignatura");
				// Se sale del frame, y se entra al frame deonde esta el boton de postular
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame(5);
				// Se postula a la asignatura
				driver.findElement(By.id("btn_postular")).click();
				if (driver.switchTo().alert().getText().compareTo("¿Está segur@ que desea inscribir la(s) coordinación(es) seleccionada(s)?") == 0 ) {
					driver.switchTo().alert().accept();
				}
				// Tiempo para guardar la asignatura postulada
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
				i = 1;
			} else {
				// No se puede postular a la asignatura
				System.out.println("	No se puede postular a la asignatura");
			}
		}
	}

	@Test
	public void Inscribir_Postulaciones() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();

		int CANTIDADASIGNATURASLIMITE = 6;

		// Se busca cuantas asignaturas lleva solicitadas y postuladas
		int contadorAsignaturas = 0;
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Solicitud Inscripción")).click();
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		String[] listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody")).get(0).getText().strip().split("\n");
		contadorAsignaturas += listadoSolicitudesEnviadas.length;
		// Seleccionar proceso de enviar solicitudes
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Postulación 1/2023")).click();
		// Seleccionar el frame con las postulaciones
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas postuladas
		List<WebElement> listadoAsignaturasPostuladas = driver.findElements(By.className("nivel"));
		// Se almacenan todas las asignaturas postuladas
		ArrayList<String> listadoAsignaturasPostuladasAux = new ArrayList<String>();
		for (WebElement linea : listadoAsignaturasPostuladas) {
			listadoAsignaturasPostuladasAux.add(linea.getText().replace("\n", " ").strip());
		}
		contadorAsignaturas += listadoAsignaturasPostuladasAux.size();





		int i = 0;
		while ( (i == 0) && ( contadorAsignaturas < CANTIDADASIGNATURASLIMITE ) ){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			for( int j = 1; j <= 3; j++ ){
				System.out.println("Vuelta " + j );
				// Seleccionar frame del listado de cursos
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				String[] listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2)")).get(0).getText().strip().split("\n");
				// Se crea una lista para almacenar todas las asignaturas de forma [codigo, nombre]
				ArrayList<ArrayList<String>> listadoCodigoNombre = new ArrayList<ArrayList<String>>();
				// Se almacenan las listas
				for (String linea : listadoAsignaturas) {
					String codigo = linea.split(" ")[0];
					String nombreAsignatura = linea.substring(codigo.length(), linea.length() - 1 ).strip();
					listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}

				// Se crea una lista para almacenar las asignaturas que no se pudieron inscribir
				ArrayList<Integer> codigosAsignaturasAOmitir = new ArrayList<Integer>();
				System.out.println("Lista codigos a omitir ");
				for( int codigo : codigosAsignaturasAOmitir ){
					System.out.println("Codigo: " + codigo );
				}

				// Se revisan todos los cursos
				int l = 0;
				while( l == 0) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					Boolean existeTeoria = false;
					Boolean existeLaboratorio = false;
					Boolean existeEjercicio = false;
					Boolean seleccionoTeoria = false;
					Boolean seleccionoLaboratorio = false;
					Boolean seleccionoEjercicio = false;
					// Se crea el random para seleccionar una asignatura al azar
					Random rand = new Random();
					int rand_int1 = rand.nextInt(listadoCodigoNombre.size());
					// Se revisa cuantas asignaturas se omitieron
					int asignaturasOmitidas = 0;
					// Mientras el while este en la lista de omitidos y no se alcance el limite, se busca otro
					while( (codigosAsignaturasAOmitir.contains(Integer.parseInt(listadoCodigoNombre.get(rand_int1).get(0)))) && (asignaturasOmitidas <= listadoCodigoNombre.size() ) ){
						rand_int1 = rand.nextInt(listadoCodigoNombre.size());
						asignaturasOmitidas++;
					}
					// Si se revisaron todas las asignaturas en una vuelta, sale de la vuelta
					if( asignaturasOmitidas > listadoCodigoNombre.size() ){
						l = 1;
					} else {
						// Se selecciona una asignatura
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("derecho");
						// Se hace click en la asignatura
						driver.findElement(By.linkText(listadoCodigoNombre.get(rand_int1).get(1))).click();
						System.out.println("\n	Asignatura: " + listadoCodigoNombre.get(rand_int1).get(0));

						// Se sale del frame, y se entra al frame de los cursos de teoria.
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("frame_cteo");
						// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
						driver.switchTo().frame("mainFrame");
						System.out.println("		Se revisan los cursos de teoria");
						// Se obtienen todos los cursos
						List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
						// Se obtiene la primera linea despues de buscar tr
						String primeraLinea = listadoTeoria.get(0).getText().strip();
						// Si la primera linea dice que no hay coordinaciones, se avisa
						if( !primeraLinea.equals("No hay coordinaciones definidas.") ){
							// Existen coordinaciones
							existeTeoria = true;
							// Se crea un random para seleccionar una seccion de teoria al azar
							int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
							// Se selecciona una seccion
							driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)")).click();
							seleccionoTeoria = true;
							System.out.println("			Si hay cursos de teoria");
						} else {
							// No hay cursos de teoria
							System.out.println("			No hay cursos de teoria");
						}

						// Se sale del frame, y se entra al frame de los cursos de laboratorio.
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("frame_clab");
						// Se selecciona el frame de los cursos (::LISTADO LABORATORIOS es un frame)
						driver.switchTo().frame("mainFrame");
						System.out.println("		Se revisan los cursos de laboratorio");
						// Se obtienen todos los cursos
						List<WebElement> listadoLaboratorio = driver.findElements(By.cssSelector("tr"));
						// Se almacenan todas las secciones
						ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
						for (WebElement e : listadoLaboratorio) {
							String tmp = e.getText().replace("\n", " ");
							if( tmp.equals("No hay coordinaciones de laboratorio definidas.") ){
								// No hay cursos de laboratorio
								System.out.println("			No hay cursos de laboratorio");
							} else {
								listadoLaboratorioAux.add(tmp);
								existeLaboratorio = true;
							}
						}
						// Se prueba seleccionar una seccion de laboratorio
						for ( int k = 0; k < listadoLaboratorioAux.size(); k++ ) {
							String seccion = listadoLaboratorioAux.get(k).replace("\n", " "	).strip();
							String[] seccionAux = seccion.split(" ");
							int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
							System.out.println("			Seccion " + (k+1) + " | " + seccion);
							System.out.println("			Seccion " + (k+1) + " | Cupos: " + cupos);
							// Se revisa si existe cupo
							if( cupos > 0 ){
								// Si hay cupos, se selecciona
								driver.findElement(By.cssSelector("tr.row-laboratorio:nth-child("+(k+1)+") > td:nth-child(1) > div:nth-child(1) > input:nth-child(1)")).click();
								seleccionoEjercicio = true;
								System.out.println("			Seccion " + (k+1) + " | Seleccionada");
								break;
							} else {
								// Si no hay cupos, se selecciona el siguiente curso
								System.out.println("			Seccion " + (k+1) + " | Sin cupos");
							}
						}

						// Se sale del frame, y se entra al frame de los cursos de ejercicio.
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("frame_ceje");
						// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
						driver.switchTo().frame("mainFrame");
						System.out.println("		Se revisan los cursos de ejercicio");
						// Se obtienen todos los cursos
						List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
						// Se almacenan todas las secciones
						ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
						for (WebElement e : listadoEjercicio) {
							String tmp = e.getText().replace("\n", " ");
							if( tmp.equals("No hay coordinaciones de ejercicios definidas.") ){
								// No hay cursos de ejercicios
								System.out.println("			No hay cursos de ejercicios");
							} else {
								listadoEjercicioAux.add(tmp);
								existeEjercicio = true;
								// Se crea un random para seleccionar una seccion de ejercicio al azar
								int rand_int2 = rand.nextInt(listadoEjercicio.size());
								// Se selecciona una seccion
								driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)")).click();
								seleccionoEjercicio = true;
							}
						}

						// Se revisa que exista al menos un curso y se haya podido seleccionar
						if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
							// Se puede postula a la asignatura
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame(5);
							// Se postula a la asignatura
							driver.findElement(By.id("btn_postular")).click();
							if (driver.switchTo().alert().getText().compareTo("¿Está segur@ que desea inscribir la(s) coordinación(es) seleccionada(s)?") == 0 ) {
								driver.switchTo().alert().accept();
								System.out.println("		Asignatura inscrita: " + listadoCodigoNombre.get(rand_int1) );
								contadorAsignaturas++;
							} else {
								System.out.println("No coincide el mensaje al inscribir la asignatura");
							}
							// Tiempo para guardar la asignatura postulada
							driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
						} else {
							System.out.println("		No es posible inscribir la asignatura");
						}
						// Se guarda el random para saber que esta asignatura no se debe repetir
						int codigo = Integer.parseInt(listadoCodigoNombre.get(rand_int1).get(0));
						codigosAsignaturasAOmitir.add(codigo);
					}
				}
			}
			i = 1;
		}
	}

	@Test
	public void Desinscribir_Random() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		// Seleccionar proceso de Inscripcion
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Postulación 1/2023")).click();
		// Se entra al frame con las asignaturas postuladas
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se buscan la cantidad de asignaturas postuladas
		List<WebElement> accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		List<WebElement> codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		int postulacionesNoDesinscribibles = 0;
		for( int i = 0; i < accionesAsignaturasPostuladas.size() ; i++ ){
			if( !accionesAsignaturasPostuladas.get(i).getText().equals("DESINSCRIBIR") ){
				postulacionesNoDesinscribibles++;
			}
		}
		ArrayList<Integer> randomOmitidos = new ArrayList<Integer>();
		int i = 0;
		while( i == 0){
			int cantidadBotones = accionesAsignaturasPostuladas.size();
			// Se crea el random para seleccionar una asignatura al azar
			Random rand = new Random();
			int rand_int = rand.nextInt(cantidadBotones);
			while( (randomOmitidos.contains(rand_int)) && (randomOmitidos.size() < postulacionesNoDesinscribibles ) ){
				rand_int = rand.nextInt(cantidadBotones);
			}
			int j = 0;
			while( j < cantidadBotones ){
				if( accionesAsignaturasPostuladas.get(j).getText().equals("DESINSCRIBIR") ){
					// Se crea el JavascriptExecutor para hacer scroll
					JavascriptExecutor js = (JavascriptExecutor) driver;
					// Se obtiene la posicion del boton
					Point location = accionesAsignaturasPostuladas.get(j).getLocation();
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+location.getY()+")");
					String codigo = codigosAsignaturasPostuladas.get(j).getText().split("-")[0].strip();
					// Se selecciona la solicitud
					accionesAsignaturasPostuladas.get(j).click();
					// Se acepa el mensaje de desinscripcion
					driver.switchTo().alert().accept();
					//Se ingresa el codigo de la asignatura
					driver.switchTo().defaultContent();
					// Tiempo para esperar que se abra la alerta
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
					driver.switchTo().alert().sendKeys(codigo);
					// Se acepta el mensaje de desinscripcion
					driver.switchTo().alert().accept();
					// Se entra al frame con las asignaturas postuladas
					driver.switchTo().defaultContent();
					driver.findElement(By.id("navbar-dropdown-procesos")).click();
					driver.findElement(By.linkText("Postulación 1/2023")).click();
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame(5);
					// Se buscan la cantidad de asignaturas restantes
					accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
					codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
					cantidadBotones = accionesAsignaturasPostuladas.size();
					j = cantidadBotones;
					i = 1;
				} else {
					j++;
				}
			}
		}
	}

	@Test
	public void Desinscribir_Todo() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		// Seleccionar proceso de Inscripcion
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Postulación 1/2023")).click();
		// Se entra al frame con las asignaturas postuladas
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se buscan la cantidad de asignaturas postuladas
		List<WebElement> accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		List<WebElement> codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		int i = 0;
		while( i == 0){
			int cantidadBotones = accionesAsignaturasPostuladas.size();
			int j = 0;
			while( j < cantidadBotones ){
				if( accionesAsignaturasPostuladas.get(j).getText().equals("DESINSCRIBIR") ){
					// Se crea el JavascriptExecutor para hacer scroll
					JavascriptExecutor js = (JavascriptExecutor) driver;
					// Se obtiene la posicion del boton
					Point location = accionesAsignaturasPostuladas.get(j).getLocation();
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+location.getY()+")");
					String codigo = codigosAsignaturasPostuladas.get(j).getText().split("-")[0].strip();
					// Se selecciona la solicitud
					accionesAsignaturasPostuladas.get(j).click();
					// Se acepa el mensaje de desinscripcion
					driver.switchTo().alert().accept();
					//Se ingresa el codigo de la asignatura
					driver.switchTo().defaultContent();
					// Tiempo para esperar que se abra la alerta
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
					driver.switchTo().alert().sendKeys(codigo);
					// Se acepta el mensaje de desinscripcion
					driver.switchTo().alert().accept();
					// Se entra al frame con las asignaturas postuladas
					driver.switchTo().defaultContent();
					driver.findElement(By.id("navbar-dropdown-procesos")).click();
					driver.findElement(By.linkText("Postulación 1/2023")).click();
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame(5);
					// Se buscan la cantidad de asignaturas restantes
					accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
					codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
					cantidadBotones = accionesAsignaturasPostuladas.size();
				} else {
					j++;
				}
			}
			i = 1;
		}
	}

	@Test
	public void Mandar_Solicitud() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Solicitud Inscripción")).click();
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame("derecho");
		// Se obtienen todas las asignaturas
		List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector("tr.bold7"));
		// Se almacenan todas las asignaturas
		ArrayList<String> listadoAsignaturasAux = new ArrayList<String>();
		for (WebElement e : listadoAsignaturas) {
			String[] asignatura = e.getText().split(" ");
			String tmp = "";
			for( int i = 1; i < asignatura.length - 1 ; i++ ) {
				tmp += " " + asignatura[i];
			}
			listadoAsignaturasAux.add(tmp.strip());
		}
		// Se revisan todos los cursos
		int i = 0;
		while( i == 0) {
			Boolean existeTeoria = false;
			Boolean existeLaboratorio = false;
			Boolean existeEjercicio = false;
			Boolean seleccionoTeoria = false;
			Boolean seleccionoLaboratorio = false;
			Boolean seleccionoEjercicio = false;
			// Se crea el random para seleccionar una asignatura al azar
			Random rand = new Random();
			int rand_int1 = rand.nextInt(listadoAsignaturasAux.size());
			// Se crea el JavascriptExecutor para hacer scroll
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Se selecciona una asignatura
			WebElement asignatura = driver.findElement(By.linkText(listadoAsignaturasAux.get(rand_int1)));
			System.out.println("\nAsignatura: " + asignatura.getText());
			// Se obtiene la posicion de la asignatura seleciconada
			Point location = asignatura.getLocation();
			// Se hace scroll hacia la asignatura
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se hace click en la asignatura
			driver.findElement(By.linkText(listadoAsignaturasAux.get(rand_int1))).click();
			System.out.println("	Se revisan los cursos de teoria");
			// Se sale del frame, y se entra al frame de los cursos de teoria.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_cteo");
			// Se selecciona el frame de los cursos (:: COORDINACIONES CURSOS es un frame)
			driver.switchTo().frame("mainFrame");
			// Se obtienen todos los cursos
			List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
			// Se almacenan todas las secciones
			ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
			for (WebElement e : listadoTeoria) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals("No hay coordinaciones de teoría definidas.") ){
					// No hay cursos de teoria
				} else {
					listadoTeoriaAux.add(tmp);
					existeTeoria = true;
					driver.findElement(By.cssSelector(".row-curso:nth-child(1) > td:nth-child(1)")).click();
					seleccionoTeoria = true;
				}
			}
			System.out.println("	Se revisan los cursos de laboratorio");
			// Se sale del frame, y se entra al frame de los cursos de laboratorio.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_clab");
			// Se selecciona el frame de los cursos (::LISTADO LABORATORIOS es un frame)
			driver.switchTo().frame("mainFrame");
			// Se obtienen todos los cursos
			List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
			// Se almacenan todas las secciones
			ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
			for (WebElement e : listadoEjercicio) {
				String tmp = e.getText().replace("\n", " ");
				// Los frames de laboratorio y ejercicio estan al revez, por lo que se esta revisando el frame de ejercicio
				if( tmp.equals("No hay coordinaciones de ejercicio definidas.") ){
					// No hay cursos de laboratorio
				} else {
					listadoEjercicioAux.add(tmp);
					existeEjercicio = true;
					driver.findElement(By.cssSelector(".row-ejercicio:nth-child(1) > td:nth-child(1)")).click();
					seleccionoLaboratorio = true;
				}
			}
			System.out.println("	Se revisan los cursos de ejercicio");
			// Se sale del frame, y se entra al frame de los cursos de ejercicio.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_ceje");
			// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
			driver.switchTo().frame("mainFrame");
			// Se obtienen todos los cursos
			List<WebElement> listadoLaboratorio = driver.findElements(By.cssSelector("tr"));
			// Se almacenan todas las secciones
			ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
			for (WebElement e : listadoLaboratorio) {
				String tmp = e.getText().replace("\n", " ");
				// Los frames de laboratorio y ejercicio estan al revez, por lo que se esta revisando el frame de ejercicio
				if( tmp.equals("No hay coordinaciones definidas.") ){
					// No hay cursos de ejercicios
				} else {
					listadoLaboratorioAux.add(tmp);
					existeLaboratorio = true;
					driver.findElement(By.cssSelector(".row-laboratorio:nth-child(1) > td:nth-child(1)")).click();
					seleccionoEjercicio = true;
				}
			}
			System.out.println("	Se revisa que se haya seleccionado al menos un curso");
			// Se revisa que exista al menos un curso
			if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
				// Se puede postular a la asignatura
				System.out.println("	Se puede postular a la asignatura");
				// Se sale del frame, y se entra al frame deonde esta el boton de postular
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_solicitudes");
				// Se ingresa el motivo de la postulacion
				driver.findElement(By.id("motivo")).click();
				driver.findElement(By.id("motivo")).sendKeys("Motivo Test");
				// Se postula a la asignatura
				driver.findElement(By.id("btn_solicitar")).click();
				if (driver.switchTo().alert().getText().compareTo("¿Está segur@ que desea solicitar la inscripción de la(s) coordinación(es) seleccionada(s)?") == 0 ) {
					driver.switchTo().alert().accept();
				}
				// Tiempo para guardar la asignatura postulada
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
				i = 1;
			} else {
				// No se puede postular a la asignatura
				System.out.println("	No se puede postular a la asignatura");
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				
			}
		}
	}

	@Test
	public void Eliminar_Solicitud() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Solicitud Inscripción")).click();
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		String[] listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody")).get(0).getText().strip().split("\n");
		// Se revisa que existan asignaturas
		if( listadoSolicitudesEnviadas.length < 0 ) {
			System.out.println("No existen solicitudes enviadas\n");
		} else {
			// Se crea el random para seleccionar una asignatura al azar
			Random rand3 = new Random();
			int rand_int3 = rand3.nextInt(listadoSolicitudesEnviadas.length);
			// Se revisa la accion que se puede realizar con la solicitud
			String lineaSolicitud = driver.findElement(By.cssSelector(".table > tbody:nth-child(2) > tr:nth-child("+(rand_int3+1)+")")).getText().split("\n")[0];
			String[] accionSolicitud = lineaSolicitud.split(" ");
			String botonSolicitud = accionSolicitud[accionSolicitud.length-1];
			// Se revisa la accion que se puede realizar con la solicitud
			if ( botonSolicitud.matches("ELIMINAR") ) {
				// Se selecciona la solicitud
				driver.findElement(By.cssSelector(".table > tbody:nth-child(2) > tr:nth-child("+(rand_int3+1)+") > td:nth-child(7)")).click();
				driver.switchTo().alert().accept();
			} else {
				System.out.println("No hay solicitudes para eliminar\n");
			}
		}
	}

	@Test
	public void Revisar_Solicitudes() {
		// Se configura el driver para firefox
		System.setProperty("webdriver.gecko.driver", "D:\\1. Descargas Internet\\Selenium\\geckodriver-v0.33.0-win64\\geckodriver.exe");
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get("https://loa.usach.cl/intranetfing/index.jsp");
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys("145010330");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys("Cbh1450");
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText("Solicitud Inscripción")).click();
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		String[] listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody")).get(0).getText().strip().split("\n");
		// Se revisa que existan asignaturas
		if( listadoSolicitudesEnviadas.length < 0 ) {
			System.out.println("No existen solicitudes enviadas\n");
		} else {
			for( int i = 1; i < listadoSolicitudesEnviadas.length + 1 ; i++ ){
				if( i > 1 ){
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame(5);
				}
				// Se revisa la accion que se puede realizar con la solicitud
				String botonSolicitud = driver.findElement(By.cssSelector(".table > tbody:nth-child(2) > tr:nth-child("+i+") > td:nth-child(7) > a:nth-child(1)")).getText().split("\n")[0];
				// Se revisa la accion que se puede realizar con la solicitud
				if ( !botonSolicitud.matches("ELIMINAR") ) {
					// Se selecciona la solicitud
					driver.findElement(By.cssSelector(".table > tbody:nth-child(2) > tr:nth-child("+i+") > td:nth-child(7) > a:nth-child(1)")).click();
					// Tiempo para que se abra la ventana emergente
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
					// Se obtiene la respuesta de la solicitud
					String respuesta = driver.findElement(By.cssSelector(".blockUI.blockMsg.blockPage")).getText();
					if( respuesta.equals("") ){
						respuesta = driver.findElement(By.id("motivo_rechazo_"+(i-1))).getText();
					}
					String[] lineaEstadoSolicitud = respuesta.split("\n")[1].split(" ");
					String estadoSolicitud = lineaEstadoSolicitud[lineaEstadoSolicitud.length-1];
					// Se imprime la respuesta
					if( estadoSolicitud.equals("RECHAZADA") ) {
						System.out.println("\nSolicitud rechazada");
						System.out.println( respuesta.split("\n")[2]);
						System.out.println( respuesta.split("\n")[3]);
					}
					// Se imprime la respuesta
					if( estadoSolicitud.equals("ACEPTADA") ) {
						System.out.println("\nSolicitud aceptada");
						System.out.println( respuesta.split("\n")[2]);
					}
					driver.switchTo().defaultContent();
					driver.findElement(By.id("navbar-dropdown-procesos")).click();
					driver.findElement(By.linkText("Solicitud Inscripción")).click();
				}
			}
		}
	}
}