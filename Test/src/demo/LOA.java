package demo;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LOA extends LOA_Vars {
	@Test
	public void Postulaciones_Inscribir_Random() {
		System.out.println("Se inicia el test Postulaciones_Inscribir_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de Inscripcion
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame("derecho");
		// Se obtienen todas las asignaturas
		List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector("tr"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
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
			// Se busca una asignatura
			WebElement asignatura = driver.findElement(By.linkText(listadoAsignaturasAux.get(rand_int1)));
			// Se crea el JavascriptExecutor para hacer scroll
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Se obtiene la posicion del boton
			Point location = asignatura.getLocation();
			// Se hace scroll hacia el boton
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se selecciona una asignatura
			asignatura.click();
			System.out.println("\n	Asignatura: " + listadoAsignaturasAux.get(rand_int1));
			try { TimeUnit.MILLISECONDS.sleep(323); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se sale del frame, y se entra al frame de los cursos de teoria.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_cteo");
			// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
			driver.switchTo().frame("mainFrame");
			System.out.println("	Se revisan los cursos de teoria");
			// Se obtienen todos los cursos
			List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se almacenan todas las secciones
			ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
			for (WebElement e : listadoTeoria) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals(LOA_Vars.sinCoordinacionInscribirTeoriaText) ){
					// No hay cursos de teoria
					System.out.println("		No hay cursos de teoria");
				} else {
					// Existen coordinaciones
					existeTeoria = true;
					// Se crea un random para seleccionar una seccion de teoria al azar
					int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
					// Se obtiene la seccion
					WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)"));
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
					// Se selecciona la seccion
					seccionElement.click();
					seleccionoTeoria = true;
					System.out.println("			Si hay cursos de teoria");
					listadoTeoriaAux.add(tmp);
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
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se almacenan todas las secciones
			ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
			for (WebElement e : listadoLaboratorio) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals(LOA_Vars.sinCoordinacionInscribirLabText) ){
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
					// Si hay cupos, se hace scroll y luego se selecciona
					WebElement seccionElement = driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(j+1)+") > td:nth-child(1)"));
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
					// Se selecciona la seccion
					seccionElement.click();
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
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se almacenan todas las secciones
			ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
			for (WebElement e : listadoEjercicio) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals(LOA_Vars.sinCoordinacionInscribirEjeText) ){
					// No hay cursos de ejercicios
					System.out.println("		No hay cursos de ejercicios");
				} else {
					// Existen coordinaciones
					existeEjercicio = true;
					// Se crea un random para seleccionar una seccion de teoria al azar
					int rand_int2 = rand.nextInt((listadoEjercicio.size() - 1) + 1) + 1;
					// Se obtiene la seccion
					WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
					// Se selecciona la seccion
					seccionElement.click();
					seleccionoEjercicio = true;
					System.out.println("			Si hay cursos de teoria");
					listadoEjercicioAux.add(tmp);
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
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				if (driver.switchTo().alert().getText().compareTo(LOA_Vars.alertaInscribirText) == 0 ) {
					driver.switchTo().alert().accept();
				}
				// Tiempo para guardar la asignatura postulada
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				i = 1;
			} else {
				// No se puede postular a la asignatura
				System.out.println("	No se puede postular a la asignatura");
			}
		}
		System.out.println("\nSe finaliza el test Postulaciones_Inscribir_Random");
	}

	@Test
	public void Postulaciones_Inscribir_Limite() {
		System.out.println("Se inicia el test Postulaciones_Inscribir_Limite");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }

																	int CANTIDADASIGNATURASLIMITE = 6;

		// Se busca cuantas asignaturas lleva solicitadas y postuladas
		int contadorAsignaturas = 0;
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		String[] listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody")).get(0).getText().strip().split("\n");
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		contadorAsignaturas += listadoSolicitudesEnviadas.length;
		// Seleccionar proceso de enviar solicitudes
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar el frame con las postulaciones
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas postuladas
		List<WebElement> listadoAsignaturasPostuladas = driver.findElements(By.className("nivel"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se almacenan todas las asignaturas postuladas
		ArrayList<String> listadoAsignaturasPostuladasAux = new ArrayList<String>();
		for (WebElement linea : listadoAsignaturasPostuladas) {
			listadoAsignaturasPostuladasAux.add(linea.getText().replace("\n", " ").strip());
		}
		contadorAsignaturas += listadoAsignaturasPostuladasAux.size();
		int i = 0;
		while ( i == 0 ){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			for( int j = 1; j <= 3; j++ ){
				System.out.println("Vuelta " + j );
				// Seleccionar frame del listado de cursos
				driver.switchTo().defaultContent();
				driver.findElement(By.id("navbar-dropdown-procesos")).click();
				driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				ArrayList<ArrayList<String>> listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
				for (WebElement linea : listadoAsignaturas2) {
					String codigo = linea.getText().split(" ")[0];
					String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
					listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}
				// Se crea una lista para almacenar las asignaturas que no se pudieron inscribir
				ArrayList<Integer> codigosAsignaturasAOmitir2 = new ArrayList<Integer>();
				// Se revisan todos los cursos
				int l = 0;
				while( (l == 0) && (contadorAsignaturas < CANTIDADASIGNATURASLIMITE) ) {
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
					int rand_int1 = rand.nextInt(listadoCodigoNombre2.size());
					// Se revisa cuantas asignaturas se omitieron
					int asignaturasOmitidas = 0;
					// Mientras el while este en la lista de omitidos y no se alcance el limite, se busca otro
					while( (codigosAsignaturasAOmitir2.contains(Integer.parseInt(listadoCodigoNombre2.get(rand_int1).get(0)))) && (asignaturasOmitidas <= listadoCodigoNombre2.size() ) ){
						rand_int1 = rand.nextInt(listadoCodigoNombre2.size());
						asignaturasOmitidas++;
					}
					// Si se revisaron todas las asignaturas en una vuelta, sale de la vuelta
					if( asignaturasOmitidas > listadoCodigoNombre2.size() ){
						l = 1;
					} else {
						// Se selecciona una asignatura
						driver.switchTo().defaultContent();
						driver.findElement(By.id("navbar-dropdown-procesos")).click();
						driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("derecho");
						// Se busca una asignatura
						WebElement asignatura = driver.findElement(By.linkText(listadoCodigoNombre2.get(rand_int1).get(1)));
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = asignatura.getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						// Se hace click en la asignatura
						asignatura.click();
						System.out.println("\n	Asignatura: " + listadoCodigoNombre2.get(rand_int1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1).get(1) );
						try { TimeUnit.MILLISECONDS.sleep(323); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se sale del frame, y se entra al frame de los cursos de teoria.
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("frame_cteo");
						// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
						driver.switchTo().frame("mainFrame");
						System.out.println("		Se revisan los cursos de teoria");
						// Se obtienen todos los cursos
						List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtiene la primera linea despues de buscar tr
						String primeraLinea = listadoTeoria.get(0).getText().strip();
						// Si la primera linea dice que no hay coordinaciones, se avisa
						if( !primeraLinea.equals(LOA_Vars.sinCoordinacionInscribirTeoriaText) ){
							// Existen coordinaciones
							existeTeoria = true;
							// Se crea un random para seleccionar una seccion de teoria al azar
							int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
							// Se obtiene la seccion
							WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)"));
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
							// Se selecciona la seccion
							seccionElement.click();
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
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se almacenan todas las secciones
						ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
						for (WebElement e : listadoLaboratorio) {
							String tmp = e.getText().replace("\n", " ");
							if( tmp.equals(LOA_Vars.sinCoordinacionInscribirLabText) ){
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
								// Si hay cupos, se hace scroll y luego se selecciona
								WebElement seccionElement = driver.findElement(By.cssSelector("tr.row-laboratorio:nth-child("+(k+1)+") > td:nth-child(1) > div:nth-child(1) > input:nth-child(1)"));
								// Se hace scroll hacia el boton
								js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
								// Se selecciona la seccion
								seccionElement.click();
								seleccionoLaboratorio = true;
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
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se almacenan todas las secciones
						ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
						for (WebElement e : listadoEjercicio) {
							String tmp = e.getText().replace("\n", " ");
							if( tmp.equals(LOA_Vars.sinCoordinacionInscribirEjeText) ){
								// No hay cursos de ejercicios
								System.out.println("			No hay cursos de ejercicios");
							} else {
								listadoEjercicioAux.add(tmp);
								existeEjercicio = true;
								// Se crea un random para seleccionar una seccion de ejercicio al azar
								int rand_int2 = rand.nextInt(listadoEjercicio.size());
								// Se obtiene la seccion
								WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
								// Se hace scroll hacia el boton
								js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
								// Se selecciona la seccion
								seccionElement.click();
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
							try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
							if (driver.switchTo().alert().getText().compareTo(LOA_Vars.alertaInscribirText) == 0 ) {
								driver.switchTo().alert().accept();
								try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
								driver.switchTo().defaultContent();
								driver.switchTo().frame("mainFrame");
								driver.switchTo().frame(5);
								// Se revisa el mensaje
								List<WebElement> mensajes = driver.findElements(By.cssSelector(".col-12 > div"));
								try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
								for (WebElement mensaje : mensajes){
									try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
									// Se revisa si el mensaje corresponde a asignatura inscrita
									if( mensaje.getText().compareTo(LOA_Vars.postulacionStatusInscritaText + Integer.parseInt(listadoCodigoNombre2.get(rand_int1).get(0)) + " (" + LOA_Vars.proceso + ")." ) == 0 ) {
										System.out.println("		Asignatura inscrita");
										// Se guarda la asignatura inscrita
										contadorAsignaturas++;
									} else {
										System.out.println("		Asignatura no inscrita");
									}
								}
							} else {
								System.out.println("No coincide el mensaje al inscribir la asignatura");
							}
							// Tiempo para guardar la asignatura postulada
							try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						} else {
							System.out.println("		No es posible inscribir la asignatura");
						}
						// Se guarda el random para saber que esta asignatura no se debe repetir
						int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1).get(0));
						codigosAsignaturasAOmitir2.add(codigo);
					}
				}
			}
			i = 1;
			System.out.println("	Se inscribieron " + contadorAsignaturas + " asignaturas");
			System.out.println("Se llego al limite de asignaturas a inscribir");
		}
		System.out.println("\nSe finaliza el test Postulaciones_Inscribir_Limite");
	}

	@Test
	public void Postulaciones_Desinscribir_Random() {
		System.out.println("Se inicia el test Postulaciones_Desinscribir_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de Inscripcion
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se entra al frame con las asignaturas postuladas
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se buscan la cantidad de asignaturas postuladas
		List<WebElement> accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		List<WebElement> codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		int postulacionesNoDesinscribibles = 0;
		for( int i = 0; i < accionesAsignaturasPostuladas.size() ; i++ ){
			if( !accionesAsignaturasPostuladas.get(i).getText().equals(LOA_Vars.btnPostulacionDESINSCRIBIRText) ){
				postulacionesNoDesinscribibles++;
			}
		}
		if( postulacionesNoDesinscribibles == accionesAsignaturasPostuladas.size() ){
			System.out.println("No hay asignaturas para desinscribir");
		} else {
			// Se crea un arraylist para guardar los randoms ya utilizados (para no repetirlos
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
					if( accionesAsignaturasPostuladas.get(j).getText().equals(LOA_Vars.btnPostulacionDESINSCRIBIRText) ){
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = accionesAsignaturasPostuladas.get(j).getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						String codigo = codigosAsignaturasPostuladas.get(j).getText().split("-")[0].strip();
						// Se selecciona la solicitud
						accionesAsignaturasPostuladas.get(j).click();
						// Se acepta el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Tiempo para esperar que se abra la alerta
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se ingresa el codigo de desinscripcion
						driver.switchTo().alert().sendKeys(codigo);
						// Se acepta el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Se entra al frame con las asignaturas postuladas
						driver.switchTo().defaultContent();
						// Tiempo para esperar que se abra la alerta
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se entra al frame con las asignaturas postuladas
						driver.switchTo().defaultContent();
						driver.findElement(By.id("navbar-dropdown-procesos")).click();
						driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame(5);
						// Se buscan la cantidad de asignaturas restantes
						accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						cantidadBotones = accionesAsignaturasPostuladas.size();
						j = cantidadBotones;
						i = 1;
					} else {
						j++;
					}
				}
			}
		}
		System.out.println("\nSe finaliza el test Postulaciones_Desinscribir_Random");
	}

	@Test
	public void Postulaciones_Desinscribir_Todo() {
		System.out.println("Se inicia el test Postulaciones_Desinscribir_Todo");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de Inscripcion
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se entra al frame con las asignaturas postuladas
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se buscan la cantidad de asignaturas postuladas
		List<WebElement> accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		List<WebElement> codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		int cantidadPostulacionesDesinscribibles = 0;
		for( WebElement accion : accionesAsignaturasPostuladas ){
			if( accion.getText().equals(LOA_Vars.btnPostulacionDESINSCRIBIRText) ){
				cantidadPostulacionesDesinscribibles++;
			}
		}
		if( cantidadPostulacionesDesinscribibles > 0 ){
			int i = 0;
			while( i == 0){
				int cantidadBotones = accionesAsignaturasPostuladas.size();
				int j = 0;
				while( j < cantidadBotones ){
					if( accionesAsignaturasPostuladas.get(j).getText().equals(LOA_Vars.btnPostulacionDESINSCRIBIRText) ){
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = accionesAsignaturasPostuladas.get(j).getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						String codigo = codigosAsignaturasPostuladas.get(j).getText().split("-")[0].strip();
						// Se selecciona la solicitud
						accionesAsignaturasPostuladas.get(j).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se acepa el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Tiempo para esperar que se abra la alerta
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se escribe el codigo de la asignatura
						driver.switchTo().alert().sendKeys(codigo);
						// Se acepta el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						//Se ingresa el codigo de la asignatura
						driver.switchTo().defaultContent();
						// Se entra al frame con las asignaturas postuladas
						driver.switchTo().defaultContent();
						driver.findElement(By.id("navbar-dropdown-procesos")).click();
						driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame(5);
						// Se buscan la cantidad de asignaturas restantes
						accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						cantidadBotones = accionesAsignaturasPostuladas.size();
					} else {
						j++;
					}
				}
				i = 1;
			}
		} else {
			System.out.println("No hay asignaturas para desinscribir");
		}
		System.out.println("\nSe finaliza el test Postulaciones_Desinscribir_Todo");
	}

	@Test
	public void Solicitudes_Inscribir_Random() {
		System.out.println("Se inicia el test Solicitudes_Inscribir_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
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
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se obtiene la posicion de la asignatura seleciconada
			Point location = asignatura.getLocation();
			// Se hace scroll hacia la asignatura
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se hace click en la asignatura
			asignatura.click();
			System.out.println("\nAsignatura: " + asignatura.getText());
			try { TimeUnit.MILLISECONDS.sleep(323); } catch (InterruptedException e) { e.printStackTrace(); }
			System.out.println("	Se revisan los cursos de teoria");
			// Se sale del frame, y se entra al frame de los cursos de teoria.
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_cteo");
			// Se selecciona el frame de los cursos (:: COORDINACIONES CURSOS es un frame)
			driver.switchTo().frame("mainFrame");
			// Se obtienen todos los cursos
			List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se almacenan todas las secciones
			ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
			for (WebElement e : listadoTeoria) {
				String tmp = e.getText().replace("\n", " ");
				if( tmp.equals(LOA_Vars.sinCoordinacionSolicitudTeoriaText) ){
					// No hay cursos de teoria
					System.out.println("			No hay cursos de teoria");
				} else {
					// Existen coordinaciones
					existeTeoria = true;
					// Se crea un random para seleccionar una seccion de teoria al azar
					int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
					// Se obtiene la seccion
					WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)"));
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
					// Se selecciona la seccion
					seccionElement.click();
					seleccionoTeoria = true;
					System.out.println("			Si hay cursos de teoria");
					listadoTeoriaAux.add(tmp);
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
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se almacenan todas las secciones
			ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
			for (WebElement e : listadoEjercicio) {
				String tmp = e.getText().replace("\n", " ");
				// Los frames de laboratorio y ejercicio estan al revez, por lo que se esta revisando el frame de ejercicio
				if( tmp.equals(LOA_Vars.sinCoordinacionSolicitudEjeText) ){
					// No hay cursos de laboratorio
				} else {
					// Existen coordinaciones
					existeEjercicio = true;
					// Se crea un random para seleccionar una seccion de teoria al azar
					int rand_int2 = rand.nextInt((listadoEjercicio.size() - 1) + 1) + 1;
					// Se obtiene la seccion
					WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
					// Se selecciona la seccion
					seccionElement.click();
					seleccionoEjercicio = true;
					System.out.println("			Si hay cursos de teoria");
					listadoEjercicioAux.add(tmp);
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
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se almacenan todas las secciones
			ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
			for (WebElement e : listadoLaboratorio) {
				String tmp = e.getText().replace("\n", " ");
				// Los frames de laboratorio y ejercicio estan al revez, por lo que se esta revisando el frame de ejercicio
				if( tmp.equals(LOA_Vars.sinCoordinacionSolicitudLabText) ){
					// No hay cursos de ejercicios
				} else {
					// Existen coordinaciones
					existeLaboratorio = true;
					// Se crea un random para seleccionar una seccion de teoria al azar
					int rand_int2 = rand.nextInt((listadoLaboratorio.size() - 1) + 1) + 1;
					// Se obtiene la seccion
					WebElement seccionElement = driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
					// Se hace scroll hacia el boton
					js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
					// Se selecciona la seccion
					seccionElement.click();
					seleccionoLaboratorio = true;
					System.out.println("			Si hay cursos de teoria");
					listadoLaboratorioAux.add(tmp);
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
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				if (driver.switchTo().alert().getText().compareTo(LOA_Vars.alertaSolicitudConfirmText) == 0 ) {
					driver.switchTo().alert().accept();
				}
				// Tiempo para guardar la asignatura postulada
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				i = 1;
			} else {
				// No se puede postular a la asignatura
				System.out.println("	No se puede postular a la asignatura");
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
			}
		}
		System.out.println("\nSe finaliza el test Solicitudes_Inscribir_Random");
	}

	@Test
	public void Solicitudes_Inscribir_Limite() {
		System.out.println("Se inicia el test Postulaciones_Inscribir_Limite");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }

																	int CANTIDADASIGNATURASLIMITE = 8;

		// Se busca cuantas asignaturas lleva solicitadas y postuladas
		int contadorAsignaturas = 0;
		// Seleccionar proceso postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.postulacionText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar el frame con las postulaciones
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se cuenta cuantas asignaturas se han postulado
		ArrayList<String> codigosSolicitudes = new ArrayList<String>();
		int i = 0;
		while ( i < listadoAsignaturasPostuladas.size() ){
			String tmp = listadoAsignaturasPostuladas.get(i).getText().split("-")[0].trim();
			if( !codigosSolicitudes.contains(tmp) ){
				codigosSolicitudes.add(tmp);
			}
			i++;
		}
		contadorAsignaturas += codigosSolicitudes.size();
		// Seleccionar proceso de enviar solicitudes
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las solicitudes enviadas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		contadorAsignaturas += listadoSolicitudesEnviadas.size();
		int j = 0;
		while( j == 0){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			for( int k = 1; k <= 3; k++ ){
				System.out.println("Vuelta " + k );
				// Seleccionar frame del listado de cursos
				driver.switchTo().defaultContent();
				driver.findElement(By.id("navbar-dropdown-procesos")).click();
				driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
				ArrayList<ArrayList<String>> listadoCodigoNombre = new ArrayList<ArrayList<String>>();
				for (WebElement linea : listadoAsignaturas) {
					String codigo = linea.getText().split(" ")[0];
					String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
					listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}
				// Se crea una lista para almacenar las asignaturas que no se pudieron inscribir
				ArrayList<Integer> codigosAsignaturasAOmitir = new ArrayList<Integer>();
				// Se revisan todos los cursos
				int l = 0;
				while( (l == 0) && (contadorAsignaturas < CANTIDADASIGNATURASLIMITE) ) {
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
						driver.findElement(By.id("navbar-dropdown-procesos")).click();
						driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("derecho");
						// Se busca la asignatura
						WebElement asignatura = driver.findElement(By.cssSelector("tr.bold7:nth-child("+(rand_int1+1)+") > td:nth-child(2) > a:nth-child(1)"));
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = asignatura.getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						// Se hace click en la asignatura
						asignatura.click();
						System.out.println("\n	Asignatura: " + driver.findElement(By.cssSelector("tr.bold7:nth-child("+(rand_int1+1)+") > td:nth-child(2) > a:nth-child(1)")).getText() );
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se sale del frame, y se entra al frame de los cursos de teoria.
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("frame_cteo");
						// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
						driver.switchTo().frame("mainFrame");
						System.out.println("		Se revisan los cursos de teoria");
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtienen todos los cursos
						List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtiene la primera linea despues de buscar tr
						String primeraLineaTeoria = listadoTeoria.get(0).getText().strip();
						// Si la primera linea dice que no hay coordinaciones, se avisa
						if( !primeraLineaTeoria.equals(LOA_Vars.sinCoordinacionSolicitudTeoriaText) ){
							// Existen coordinaciones
							existeTeoria = true;
							// Se crea un random para seleccionar una seccion de teoria al azar
							int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
							// Se obtiene la seccion
							WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)"));
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
							// Se selecciona la seccion
							seccionElement.click();
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
						System.out.println("		Se revisan los cursos de ejercicio");
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtienen todos los cursos
						List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtiene la primera linea despues de buscar tr
						String primeraLineaEje = listadoEjercicio.get(0).getText().strip();
						// Si la primera linea dice que no hay coordinaciones, se avisa
						if( !primeraLineaEje.equals(LOA_Vars.sinCoordinacionSolicitudEjeText) ){
							// Existen coordinaciones
							existeEjercicio = true;
							// Se crea un random para seleccionar una seccion de teoria al azar
							int rand_int2 = rand.nextInt((listadoEjercicio.size() - 1) + 1) + 1;
							// Se obtiene la seccion
							WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
							// Se selecciona la seccion
							seccionElement.click();
							seleccionoEjercicio = true;
							System.out.println("			Si hay cursos de ejercicios");
						} else {
							// No hay cursos de teoria
							System.out.println("			No hay cursos de ejercicios");
						}
						// Se sale del frame, y se entra al frame de los cursos de ejercicio.
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame("frame_ceje");
						// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
						driver.switchTo().frame("mainFrame");
						System.out.println("		Se revisan los cursos de laboratorio");
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtienen todos los cursos
						List<WebElement> listadoLab = driver.findElements(By.cssSelector("tr"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se obtiene la primera linea despues de buscar tr
						String primeraLineaLab = listadoLab.get(0).getText().strip();
						// Si la primera linea dice que no hay coordinaciones, se avisa
						if( !primeraLineaLab.equals(LOA_Vars.sinCoordinacionSolicitudLabText) ){
							// Existen coordinaciones
							existeLaboratorio = true;
							// Se crea un random para seleccionar una seccion de teoria al azar
							int rand_int2 = rand.nextInt((listadoLab.size() - 1) + 1) + 1;
							// Se obtiene la seccion
							WebElement seccionElement = driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
							// Se selecciona la seccion
							seccionElement.click();
							seleccionoLaboratorio = true;
							System.out.println("			Si hay cursos de laboratorio");
						} else {
							// No hay cursos de teoria
							System.out.println("			No hay cursos de laboratorio");
						}
							if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
							// Se puede postular a la asignatura
							System.out.println("	Se puede solicitar la asignatura");
							// Se sale del frame, y se entra al frame deonde esta el boton de postular
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_solicitudes");
							// Se ingresa el motivo de la postulacion
							driver.findElement(By.id("motivo")).click();
							driver.findElement(By.id("motivo")).sendKeys("Motivo Test");
							// Se postula a la asignatura
							driver.findElement(By.id("btn_solicitar")).click();
							try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
							if (driver.switchTo().alert().getText().compareTo(LOA_Vars.alertaSolicitudConfirmText) == 0 ) {
								driver.switchTo().alert().accept();
							}
							contadorAsignaturas++;
							// Tiempo para guardar la asignatura postulada
							try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
							i = 1;
						} else {
							// No se puede postular a la asignatura
							System.out.println("	No se puede postular a la asignatura");
							
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("derecho");
						}
						// Se guarda el random para saber que esta asignatura no se debe repetir
						int codigo = Integer.parseInt(listadoCodigoNombre.get(rand_int1).get(0));
						codigosAsignaturasAOmitir.add(codigo);
					}
				}
			}
			j = 1;
			System.out.println("	Se inscribieron " + contadorAsignaturas + " asignaturas");
			System.out.println("Se llego al limite de asignaturas a inscribir");
		}
		System.out.println("\nSe finaliza el test Postulaciones_Inscribir_Limite");
	}

	@Test
	public void Solicitudes_Eliminar_Random() {
		System.out.println("Se inicia el test Solicitudes_Eliminar_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se obtienen los ids de las solicitudes eliminables
		ArrayList<Integer> listadoSolicitudesEliminables = new ArrayList<Integer>();
		for( int i = 0; i < listadoSolicitudesEnviadas.size(); i++ ) {
			if( listadoSolicitudesEnviadas.get(i).getText().equals(LOA_Vars.btnPostulacionELIMINARText) ){
				listadoSolicitudesEliminables.add(i);
			}
		}
		if( listadoSolicitudesEliminables.size() > 0 ){
			// Se crea un random para seleccionar una solicitud eliminable al azar
			Random rand = new Random();
			int rand_int = listadoSolicitudesEliminables.get(rand.nextInt(listadoSolicitudesEliminables.size()));
			// Se crea el JavascriptExecutor para hacer scroll
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Se obtiene la posicion del boton
			Point location = listadoSolicitudesEnviadas.get(rand_int).getLocation();
			// Se hace scroll hacia el boton
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se elimina la solicitud
			listadoSolicitudesEnviadas.get(rand_int).click();
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			driver.switchTo().alert().accept();
		} else {
			System.out.println("No hay solicitudes eliminables");
		}
		System.out.println("\nSe finaliza el test Solicitudes_Eliminar_Random");
	}

	@Test
	public void Solicitudes_Eliminar_Todo() {
		System.out.println("Se inicia el test Solicitudes_Eliminar_Todo");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		driver.switchTo().frame(5);
		// Se obtienen todas las postulaciones
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		int cantidadSolicitudesEliminables = 0;
		for( WebElement solicitud : listadoSolicitudesEnviadas ){
			if( solicitud.getText().equals(LOA_Vars.btnSolicitudELIMINARText) ){
				cantidadSolicitudesEliminables++;
			}
		}
		if( cantidadSolicitudesEliminables > 0 ){
			int i = 0;
			while( i == 0){
				int cantidadBotones = listadoSolicitudesEnviadas.size();
				int j = 0;
				while( j < cantidadBotones ){
					if( listadoSolicitudesEnviadas.get(j).getText().equals(LOA_Vars.btnPostulacionELIMINARText) ){
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = listadoSolicitudesEnviadas.get(j).getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						// Se selecciona la solicitud
						listadoSolicitudesEnviadas.get(j).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						// Se acepa el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Se entra al frame con las asignaturas postuladas
						driver.switchTo().defaultContent();
						driver.findElement(By.id("navbar-dropdown-procesos")).click();
						driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame(5);
						// Se buscan la cantidad de asignaturas restantes
						listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
						try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
						cantidadBotones = listadoSolicitudesEnviadas.size();
					} else {
						j++;
					}
				}
				i = 1;
			}
		} else {
			System.out.println("No hay solicitudes eliminables");
		}
		System.out.println("\nSe finaliza el test Solicitudes_Eliminar_Todo");
	}

	@Test
	public void Solicitudes_Revisar_Random() {
		System.out.println("Se inicia el test Solicitudes_Revisar_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se obtienen los ids de las solicitudes revisables
		ArrayList<Integer> listadoSolicitudesRevisables = new ArrayList<Integer>();
		for( int i = 0; i < listadoSolicitudesEnviadas.size(); i++ ) {
			if( listadoSolicitudesEnviadas.get(i).getText().equals(LOA_Vars.btnSolicitudRESPUESTAText) ){
				listadoSolicitudesRevisables.add(i);
			}
		}
		// Se crea un random para seleccionar una solicitud revisable al azar
		Random rand = new Random();
		int rand_int = listadoSolicitudesRevisables.get(rand.nextInt(listadoSolicitudesRevisables.size()));
		// Se crea el JavascriptExecutor para hacer scroll
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Se obtiene la posicion del boton
		Point location = listadoSolicitudesEnviadas.get(rand_int).getLocation();
		// Se hace scroll hacia el boton
		js.executeScript("window.scrollBy(0,"+location.getY()+")");
		// Se abre la solicitud
		listadoSolicitudesEnviadas.get(rand_int).click();
		// Tiempo para que se abra la ventana emergente
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se obtienen las lineas de respuesta
		List<WebElement> lineasRespuesta = driver.findElements(By.cssSelector("#motivo_rechazo_"+rand_int+" > table:nth-child(1) > tbody > tr"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se obtiene el codigo y nombre de la asignatura de la solicitud
		System.out.println("Solicitud:");
		String codigoAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[0];
		String estadoSolictudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[ lineasRespuesta.get(0).getText().strip().split(" ").length - 1 ];
		String nombreAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().substring(codigoAsignaturaSolicitudRevisada.length(), lineasRespuesta.get(0).getText().strip().length() - estadoSolictudRevisada.length() - 1).strip();
		System.out.println("	----------------------------------------");
		System.out.println("	Seccion:    " + codigoAsignaturaSolicitudRevisada);
		System.out.println("	Asignatura: " + nombreAsignaturaSolicitudRevisada);
		System.out.println("	Estado:     " + estadoSolictudRevisada);
		System.out.println("	----------------------------------------");
		String[] lineaRespuestasStrings = lineasRespuesta.get(1).getText().strip().split("\n");
		for( int i = 0; i < lineaRespuestasStrings.length - 1; i++ ) {
			System.out.println("		" + lineaRespuestasStrings[i].strip());
		}
		System.out.println("	----------------------------------------");
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("\nSe finaliza el test Solicitudes_Revisar_Random");
	}

	@Test
	public void Solicitudes_Revisar_Todo() {
		System.out.println("Se inicia el test Solicitudes_Revisar_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, LOA_Vars.driverPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(LOA_Vars.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(LOA_Vars.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(LOA_Vars.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar proceso de enviar solicitudes
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		// Se obtienen los ids de las solicitudes revisables
		ArrayList<Integer> listadoSolicitudesRevisables = new ArrayList<Integer>();
		for( int i = 0; i < listadoSolicitudesEnviadas.size(); i++ ) {
			if( listadoSolicitudesEnviadas.get(i).getText().equals(LOA_Vars.btnSolicitudRESPUESTAText) ){
				listadoSolicitudesRevisables.add(i);
			}
		}
		// Se crea el JavascriptExecutor para hacer scroll
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for( int solicitud : listadoSolicitudesRevisables ){
			driver.switchTo().defaultContent();
			driver.findElement(By.id("navbar-dropdown-procesos")).click();
			driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame(5);
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se obtienen todas las asignaturas
			List<WebElement> listadoSolicitudesEnviadasAux = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se obtiene la posicion del boton
			Point location = listadoSolicitudesEnviadasAux.get(solicitud).getLocation();
			// Se hace scroll hacia el boton
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se abre la solicitud
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			listadoSolicitudesEnviadasAux.get(solicitud).click();
			// Tiempo para que se abra la ventana emergente
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se obtienen las lineas de respuesta
			List<WebElement> lineasRespuesta = driver.findElements(By.cssSelector("#motivo_rechazo_"+solicitud+" > table:nth-child(1) > tbody > tr"));
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
			// Se obtiene el codigo y nombre de la asignatura de la solicitud
			System.out.println("Solicitud:");
			String codigoAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[0];
			String estadoSolictudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[ lineasRespuesta.get(0).getText().strip().split(" ").length - 1 ];
			String nombreAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().substring(codigoAsignaturaSolicitudRevisada.length(), lineasRespuesta.get(0).getText().strip().length() - estadoSolictudRevisada.length() - 1).strip();
			System.out.println("	----------------------------------------");
			System.out.println("	Seccion:    " + codigoAsignaturaSolicitudRevisada);
			System.out.println("	Asignatura: " + nombreAsignaturaSolicitudRevisada);
			System.out.println("	Estado:     " + estadoSolictudRevisada);
			System.out.println("	----------------------------------------");
			String[] lineaRespuestasStrings = lineasRespuesta.get(1).getText().strip().split("\n");
			for( int i = 0; i < lineaRespuestasStrings.length - 1; i++ ) {
				System.out.println("		" + lineaRespuestasStrings[i].strip());
			}
			System.out.println("	----------------------------------------");
			driver.switchTo().defaultContent();
			driver.findElement(By.id("navbar-dropdown-procesos")).click();
			driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
			try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		driver.findElement(By.linkText(LOA_Vars.solicitudText)).click();
		try { TimeUnit.MILLISECONDS.sleep(123); } catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("\nSe finaliza el test Solicitudes_Revisar_Todo");
	}
}