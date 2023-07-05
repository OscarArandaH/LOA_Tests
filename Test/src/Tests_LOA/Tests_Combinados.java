package Tests_LOA;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
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

// import org.junit.Rule;
// import org.junit.runner.RunWith;
// import com.google.code.tempusfugit.concurrency.ConcurrentRule;
// import com.google.code.tempusfugit.concurrency.ConcurrentTestRunner;
// import com.google.code.tempusfugit.concurrency.RepeatingRule;
// import com.google.code.tempusfugit.concurrency.annotations.Repeating;
// import com.google.code.tempusfugit.concurrency.annotations.Concurrent;

//@RunWith(ConcurrentTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests_Combinados {
  @Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_1_Postular_Solicitud_Limite() throws InterruptedException {
		System.out.println("Se inicia el test Test_1_Postular_Solicitud_Limite");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Vars_LOA.driverPath);
		System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY, Vars_LOA.logPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(Vars_LOA.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(Vars_LOA.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(Vars_LOA.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		TimeUnit.MILLISECONDS.sleep(250);
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		TimeUnit.MILLISECONDS.sleep(250);

		int CANTIDADASIGNATURASLIMITE = 8;

		// Se busca cuantas asignaturas lleva solicitadas y postuladas
		int contadorAsignaturas = 0;
    // Se crea una lista para los codigos de las asingaturas postuladas
    List<Integer> codigosAsignaturasPostuladas = new ArrayList<Integer>();
		Boolean procesoSolicitudes = true;
		Boolean procesoInscripciones = true;
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		try {
			driver.findElement(By.linkText(Vars_LOA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de postulacion");
			System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		// Se revisa si el proceso esta abierto
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try {
			driver.switchTo().frame("derecho");
		} catch (Exception e2) {
			System.out.println("El proceso se encuentra cerrado");
			System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(250);
		// Seleccionar proceso solicitudes
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		try {
			driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de solicitudes");
			procesoSolicitudes = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(250);
		// Seleccionar proceso inscripciones
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		try {
			driver.findElement(By.linkText(Vars_LOA.inscripcionText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de inscripcion");
			procesoInscripciones = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(250);
		// Si esta abierto el proceso de solicitudes, se cuentan cuantas solicitudes hay
		if ( procesoSolicitudes ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.id("navbar-dropdown-procesos")).click();
			driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
			TimeUnit.MILLISECONDS.sleep(250);
			// Seleccionar el frame con las solicitudes
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame(5);
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasSolicitadas = driver.findElements(By.className("nivel"));
			TimeUnit.MILLISECONDS.sleep(250);
			// Se almacenan todas las asignaturas solicitadas
			ArrayList<String> codigosSolicitudes = new ArrayList<String>();
			int i = 0;
			while ( i < listadoAsignaturasSolicitadas.size() ){
				String tmp = listadoAsignaturasSolicitadas.get(i).getText().split("-")[0].trim();
				if( !codigosSolicitudes.contains(tmp) ){
					codigosSolicitudes.add(tmp);
          // Se guarda el codigo de la asignatura postulada
          codigosAsignaturasPostuladas.add(Integer.parseInt(tmp));
				}
				i++;
			}
			contadorAsignaturas += codigosSolicitudes.size();
		}
		TimeUnit.MILLISECONDS.sleep(250);
		// Si esta abierto el proceso de inscripciones, se cuentan cuantas inscripciones hay
		if ( procesoInscripciones ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.id("navbar-dropdown-procesos")).click();
			driver.findElement(By.linkText(Vars_LOA.inscripcionText)).click();
			TimeUnit.MILLISECONDS.sleep(250);
			// Seleccionar el frame con las inscripciones
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_inscripcion");
			TimeUnit.MILLISECONDS.sleep(250);
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasInscritas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
			// Se cuenta cuantas asignaturas se han inscrito
			ArrayList<String> codigosInscripciones = new ArrayList<String>();
			int i = 0;
			while ( i < listadoAsignaturasInscritas.size() ){
				String tmp = listadoAsignaturasInscritas.get(i).getText().split("-")[0].trim();
				if( !codigosInscripciones.contains(tmp) ){
					codigosInscripciones.add(tmp);
          // Se guarda el codigo de la asignatura postulada
          codigosAsignaturasPostuladas.add(Integer.parseInt(tmp));
				}
				i++;
			}
			contadorAsignaturas += codigosInscripciones.size();
		}
		TimeUnit.MILLISECONDS.sleep(250);
    // Se crea una lista para los codigos de las asignaturas a omitir
    List<Integer> codigosAsignaturasOmitidas = new ArrayList<Integer>();
    int i = 0;
		while ( i == 0 ){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			int j = 0;
			while( j < 3 ){
				System.out.println("\nVuelta " + (j+1) );
				// Se revisa si se puede seleccionar el proceso de postulaciones
				driver.switchTo().defaultContent();
				driver.findElement(By.id("navbar-dropdown-procesos")).click();
				try {
					driver.findElement(By.linkText(Vars_LOA.postulacionText)).click();
				} catch (Exception e) {
					System.out.println("No se encuentra el proceso de postulacion");
					System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				// Se revisa si el proceso esta abierto
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				try {
					driver.switchTo().frame("derecho");
				} catch (Exception e2) {
					System.out.println("El proceso se encuentra cerrado");
					System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				driver.switchTo().defaultContent();
				TimeUnit.MILLISECONDS.sleep(250);
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				TimeUnit.MILLISECONDS.sleep(250);
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
				while( (l == 0) && (contadorAsignaturas <= CANTIDADASIGNATURASLIMITE) ) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
					TimeUnit.MILLISECONDS.sleep(250);
					listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
					for (WebElement linea : listadoAsignaturas2) {
						String codigo = linea.getText().split(" ")[0];
						String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
						listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
					}
					if( listadoCodigoNombre2.size() == 0 ){
						System.out.println("	No hay asignaturas disponibles para postular");
						l = 1;
					} else {
						Boolean existeTeoria = false;
						Boolean existeLaboratorio = false;
						Boolean existeEjercicio = false;
						Boolean seleccionoTeoria = false;
						Boolean seleccionoLaboratorio = false;
						Boolean seleccionoEjercicio = false;
						// Se crea el random para seleccionar una asignatura al azar
						Random rand = new Random();
						int rand_int1 = rand.nextInt((listadoCodigoNombre2.size() - 1) + 1) + 1;
						// Se revisa cuantas asignaturas se omitieron
						int asignaturasOmitidas = 0;
						// Mientras el while este en la lista de omitidos y no se alcance el limite, se busca otro
						while( (codigosAsignaturasAOmitir2.contains(Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0)))) && (asignaturasOmitidas <= listadoCodigoNombre2.size() ) ){
							rand_int1 = rand.nextInt((listadoCodigoNombre2.size() - 1) + 1) + 1;
							asignaturasOmitidas++;
						}
						// Si se revisaron todas las asignaturas en una vuelta, sale de la vuelta
						if( asignaturasOmitidas > listadoCodigoNombre2.size() ){
							l = 1;
						} else {
							// Se revisa si se puede seleccionar el proceso de postulaciones
							driver.switchTo().defaultContent();
							driver.findElement(By.id("navbar-dropdown-procesos")).click();
							try {
								driver.findElement(By.linkText(Vars_LOA.postulacionText)).click();
							} catch (Exception e) {
								System.out.println("No se encuentra el proceso de postulacion");
								System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se revisa si el proceso esta abierto
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							try {
								driver.switchTo().frame("derecho");
							} catch (Exception e2) {
								System.out.println("El proceso se encuentra cerrado");
								System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							driver.switchTo().defaultContent();
							TimeUnit.MILLISECONDS.sleep(250);
							// Se entra al frame de los derechos
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("derecho");
							// Se busca una asignatura
							WebElement asignatura = driver.findElement(By.linkText(listadoCodigoNombre2.get(rand_int1-1).get(1)));
							// Se crea el JavascriptExecutor para hacer scroll
							JavascriptExecutor js = (JavascriptExecutor) driver;
							// Se obtiene la posicion del boton
							Point location = asignatura.getLocation();
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+location.getY()+")");
							// Se hace click en la asignatura
							asignatura.click();
							System.out.println("\n	Asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) );
							TimeUnit.MILLISECONDS.sleep(250);
							// Se sale del frame, y se entra al frame de los cursos de teoria.
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_cteo");
							// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
							driver.switchTo().frame("mainFrame");
							System.out.println("		Se revisan los cursos de teoria");
							// Se obtienen todos los cursos
							List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
							TimeUnit.MILLISECONDS.sleep(250);
							// Se obtiene la primera linea despues de buscar tr
							String primeraLinea = listadoTeoria.get(0).getText().strip();
							// Si la primera linea dice que no hay coordinaciones, se avisa
							if( !primeraLinea.equals(Vars_LOA.sinCoordinacionInscribirTeoriaText) ){
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
							TimeUnit.MILLISECONDS.sleep(250);
							// Se almacenan todas las secciones
							ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
							for (WebElement e : listadoLaboratorio) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_LOA.sinCoordinacionInscribirLabText) ){
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
							TimeUnit.MILLISECONDS.sleep(250);
							// Se almacenan todas las secciones
							ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
							for (WebElement e : listadoEjercicio) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_LOA.sinCoordinacionInscribirEjeText) ){
									// No hay cursos de ejercicios
									System.out.println("			No hay cursos de ejercicios");
								} else {
									listadoEjercicioAux.add(tmp);
									existeEjercicio = true;
									// Se crea un random para seleccionar una seccion de ejercicio al azar
									int rand_int2 = rand.nextInt((listadoEjercicio.size() - 1) + 1) + 1;
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
								TimeUnit.MILLISECONDS.sleep(250);
								if (driver.switchTo().alert().getText().compareTo(Vars_LOA.alertaInscribirText) == 0 ) {
									driver.switchTo().alert().accept();
									TimeUnit.MILLISECONDS.sleep(250);
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame(5);
									// Se revisa el mensaje
									List<WebElement> mensajes = driver.findElements(By.cssSelector(".col-12 > div"));
									TimeUnit.MILLISECONDS.sleep(250);
									for (WebElement mensaje : mensajes){
										TimeUnit.MILLISECONDS.sleep(250);
										// Se revisa si el mensaje corresponde a asignatura inscrita
										if( mensaje.getText().compareTo(Vars_LOA.postulacionStatusInscritaText + Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0)) + " (" + Vars_LOA.proceso + ")." ) == 0 ) {
											System.out.println("	Asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) + " inscrita." );
											// Se guarda la asignatura inscrita
											int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
											codigosAsignaturasPostuladas.add(codigo);
											contadorAsignaturas++;
										} else {
											System.out.println("	Asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) + " NO inscrita." );
                      // Se guarda el codigo para intentar mandarlo por solicitud
                      int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
											if( !codigosAsignaturasOmitidas.contains(codigo) ){
												codigosAsignaturasOmitidas.add(codigo);
											}
										}
									}
								} else {
									System.out.println("No coincide el mensaje al inscribir la asignatura");
								}
								// Tiempo para guardar la asignatura postulada
								TimeUnit.MILLISECONDS.sleep(250);
							} else {
                System.out.println("	No es posible inscribir la asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) );
                // Se guarda el codigo para intentar mandarlo por solicitud
                int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
                if( !codigosAsignaturasOmitidas.contains(codigo) ){
									codigosAsignaturasOmitidas.add(codigo);
								}
							}
							// Se guarda el random para saber que esta asignatura no se debe repetir
							int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
							codigosAsignaturasAOmitir2.add(codigo);
						}
					}
				}
				j++;
			}
			i = 1;
			System.out.println("\n	Se inscribieron " + contadorAsignaturas + " asignaturas");
			
			int k = 0;
			while( k < codigosAsignaturasOmitidas.size() ){
				if( codigosAsignaturasPostuladas.contains(codigosAsignaturasOmitidas.get(k)) ){
					codigosAsignaturasOmitidas.remove(k);
				}
				k++;
			}
      // Si no se llego al limite de asignaturas se mandan solicitudes
      if( contadorAsignaturas <= CANTIDADASIGNATURASLIMITE ){
				// Se intenta mandar solicitud
        System.out.println("	Se intenta mandar solicitud para las asignaturas omitidas: ");
				for ( int codigoAsignatura : codigosAsignaturasOmitidas ){
					System.out.println("		" + codigoAsignatura);
				}
				int asignaturasPorMandarSolicitud = codigosAsignaturasOmitidas.size();
        // Se revisa si se puede seleccionar el proceso de postulaciones
        driver.switchTo().defaultContent();
        driver.findElement(By.id("navbar-dropdown-procesos")).click();
        try {
          driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
        } catch (Exception e) {
          System.out.println("No se encuentra el proceso de solicitudes");
          System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
          driver.quit();
          return;
        }
        // Se revisa si el proceso esta abierto
        driver.switchTo().defaultContent();
        driver.switchTo().frame("mainFrame");
        try {
          driver.switchTo().frame("derecho");
        } catch (Exception e2) {
          System.out.println("El proceso de solicitudes se encuentra cerrado");
          System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
          driver.quit();
          return;
        }
        // Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				TimeUnit.MILLISECONDS.sleep(250);
				ArrayList<ArrayList<String>> listadoCodigoNombre = new ArrayList<ArrayList<String>>();
				for (WebElement linea : listadoAsignaturas) {
					String codigo = linea.getText().split(" ")[0];
					String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
					listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}
				// Se revisan todos los cursos
				int l = 0;
				while( (l == 0) && (contadorAsignaturas < CANTIDADASIGNATURASLIMITE) && (asignaturasPorMandarSolicitud > 0) ) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
					TimeUnit.MILLISECONDS.sleep(250);
					listadoCodigoNombre = new ArrayList<ArrayList<String>>();
					for (WebElement linea : listadoAsignaturas) {
						String codigo = linea.getText().split(" ")[0];
						String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
						listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
					}
					if( listadoCodigoNombre.size() == 0 ){
						System.out.println("	No hay asignaturas disponibles para inscribirse");
						l = 1;
					} else {
						int m = 0;
						while( m < asignaturasPorMandarSolicitud ){
							// Se revisa si se puede seleccionar el proceso de postulaciones
							driver.switchTo().defaultContent();
							driver.findElement(By.id("navbar-dropdown-procesos")).click();
							try {
								driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
							} catch (Exception e) {
								System.out.println("No se encuentra el proceso de solicitudes");
								System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se revisa si el proceso esta abierto
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							try {
								driver.switchTo().frame("derecho");
							} catch (Exception e2) {
								System.out.println("El proceso de solicitudes se encuentra cerrado");
								System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se obtienen todas las asignaturas
							List<WebElement> listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
							TimeUnit.MILLISECONDS.sleep(250);
							ArrayList<ArrayList<String>> listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
							for (WebElement linea : listadoAsignaturas2) {
								String codigo = linea.getText().split(" ")[0];
								String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
								listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
							}
							int o = 0;
							System.out.println("\n	Se intenta mandar solicitud para la asignatura: " + codigosAsignaturasOmitidas.get(m));
							while ( o < listadoCodigoNombre2.size() ){
								if( Integer.parseInt(listadoCodigoNombre2.get(o).get(0)) == codigosAsignaturasOmitidas.get(m) ){
									// Se busca la asignatura
									WebElement asignatura = driver.findElement(By.cssSelector("tr.bold7:nth-child("+(o+1)+") > td:nth-child(2) > a:nth-child(1)"));
									// Se crea el JavascriptExecutor para hacer scroll
									JavascriptExecutor js = (JavascriptExecutor) driver;
									// Se obtiene la posicion del boton
									Point location = asignatura.getLocation();
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+location.getY()+")");
									// Se hace click en la asignatura
									asignatura.click();
									TimeUnit.MILLISECONDS.sleep(250);
									Boolean existeTeoria = false;
									Boolean existeLaboratorio = false;
									Boolean existeEjercicio = false;
									Boolean seleccionoTeoria = false;
									Boolean seleccionoLaboratorio = false;
									Boolean seleccionoEjercicio = false;
									Random rand = new Random();
									// Se sale del frame, y se entra al frame de los cursos de teoria.
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame("frame_cteo");
									// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
									driver.switchTo().frame("mainFrame");
									System.out.println("		Se revisan los cursos de teoria");
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtienen todos los cursos
									List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtiene la primera linea despues de buscar tr
									String primeraLineaTeoria = listadoTeoria.get(0).getText().strip();
									// Si la primera linea dice que no hay coordinaciones, se avisa
									if( !primeraLineaTeoria.equals(Vars_LOA.sinCoordinacionSolicitudTeoriaText) ){
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
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtienen todos los cursos
									List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtiene la primera linea despues de buscar tr
									String primeraLineaEje = listadoEjercicio.get(0).getText().strip();
									// Si la primera linea dice que no hay coordinaciones, se avisa
									if( !primeraLineaEje.equals(Vars_LOA.sinCoordinacionSolicitudEjeText) ){
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
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtienen todos los cursos
									List<WebElement> listadoLab = driver.findElements(By.cssSelector("tr"));
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtiene la primera linea despues de buscar tr
									String primeraLineaLab = listadoLab.get(0).getText().strip();
									// Si la primera linea dice que no hay coordinaciones, se avisa
									if( !primeraLineaLab.equals(Vars_LOA.sinCoordinacionSolicitudLabText) ){
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
										System.out.println("	Se solicitó la asignatura");
										// Se sale del frame, y se entra al frame deonde esta el boton de postular
										driver.switchTo().defaultContent();
										driver.switchTo().frame("mainFrame");
										driver.switchTo().frame("frame_solicitudes");
										// Se ingresa el motivo de la postulacion
										driver.findElement(By.id("motivo")).click();
										driver.findElement(By.id("motivo")).sendKeys("Motivo Test");
										// Se postula a la asignatura
										driver.findElement(By.id("btn_solicitar")).click();
										TimeUnit.MILLISECONDS.sleep(250);
										if (driver.switchTo().alert().getText().compareTo(Vars_LOA.alertaSolicitudConfirmText) == 0 ) {
											driver.switchTo().alert().accept();
										}
										contadorAsignaturas++;
										// Tiempo para guardar la asignatura postulada
										TimeUnit.MILLISECONDS.sleep(250);
									} else {
										// No se puede postular a la asignatura
										System.out.println("	No se puedó solicitar la asignatura");
										driver.switchTo().defaultContent();
										driver.switchTo().frame("mainFrame");
										driver.switchTo().frame("derecho");
									}
									o = listadoCodigoNombre.size();
									m++;
								} else {
									o++;
								}
							}
						}
					}
				}
      } else {
        System.out.println("    Se llego al limite de asignaturas");
      }
		}
		System.out.println("Se finaliza el test Test_1_Postular_Solicitud_Limite\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_2_Inscribir_Solicitud_Limite() throws InterruptedException {
		System.out.println("Se inicia el test Test_2_Inscribir_Solicitud_Limite");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Vars_LOA.driverPath);
		System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY, Vars_LOA.logPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(Vars_LOA.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(Vars_LOA.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(Vars_LOA.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		TimeUnit.MILLISECONDS.sleep(250);
		// Seleccionar carrera
		driver.findElement(By.linkText("1368 - INGENIERIA CIVIL OBRAS CIVILES")).click();
		TimeUnit.MILLISECONDS.sleep(250);

		int CANTIDADASIGNATURASLIMITE = 8;

		// Se busca cuantas asignaturas lleva solicitadas y inscritas
		int contadorAsignaturas = 0;
    // Se crea una lista para los codigos de las asingaturas inscritas
    List<Integer> codigosAsignaturasInscritos = new ArrayList<Integer>();
		Boolean procesoSolicitudes = true;
		Boolean procesoPostulacion = true;
		// Se revisa si se puede seleccionar el proceso de inscripciones
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		try {
			driver.findElement(By.linkText(Vars_LOA.inscripcionText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de inscripcion");
			System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		// Se revisa si el proceso esta abierto
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try {
			driver.switchTo().frame("derecho");
		} catch (Exception e2) {
			System.out.println("El proceso se encuentra cerrado");
			System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(250);
		// Seleccionar proceso solicitudes
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		try {
			driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de solicitudes");
			procesoSolicitudes = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(250);
		// Seleccionar proceso postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.id("navbar-dropdown-procesos")).click();
		try {
			driver.findElement(By.linkText(Vars_LOA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de postulacion");
			procesoPostulacion = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(250);
		// Si esta abierto el proceso de solicitudes, se cuentan cuantas solicitudes hay
		if ( procesoSolicitudes ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.id("navbar-dropdown-procesos")).click();
			driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
			TimeUnit.MILLISECONDS.sleep(250);
			// Seleccionar el frame con las solicitudes
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame(5);
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasSolicitadas = driver.findElements(By.className("nivel"));
			TimeUnit.MILLISECONDS.sleep(250);
			// Se almacenan todas las asignaturas postuladas
			ArrayList<String> codigosSolicitudes = new ArrayList<String>();
			int i = 0;
			while ( i < listadoAsignaturasSolicitadas.size() ){
				String tmp = listadoAsignaturasSolicitadas.get(i).getText().split("-")[0].trim();
				if( !codigosSolicitudes.contains(tmp) ){
					codigosSolicitudes.add(tmp);
          // Se guarda el codigo de la asignatura postulada
          codigosAsignaturasInscritos.add(Integer.parseInt(tmp));
				}
				i++;
			}
			contadorAsignaturas += codigosSolicitudes.size();
		}
		TimeUnit.MILLISECONDS.sleep(250);
		// Si esta abierto el proceso de postulaciones, se cuentan cuantas postulaciones hay
		if ( procesoPostulacion ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.id("navbar-dropdown-procesos")).click();
			driver.findElement(By.linkText(Vars_LOA.postulacionText)).click();
			TimeUnit.MILLISECONDS.sleep(250);
			// Seleccionar el frame con las inscripciones
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_inscripcion");
			TimeUnit.MILLISECONDS.sleep(250);
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
			// Se cuenta cuantas asignaturas se han postulado
			ArrayList<String> codigosInscripciones = new ArrayList<String>();
			int i = 0;
			while ( i < listadoAsignaturasPostuladas.size() ){
				String tmp = listadoAsignaturasPostuladas.get(i).getText().split("-")[0].trim();
				if( !codigosInscripciones.contains(tmp) ){
					codigosInscripciones.add(tmp);
          // Se guarda el codigo de la asignatura postulada
          codigosAsignaturasInscritos.add(Integer.parseInt(tmp));
				}
				i++;
			}
			contadorAsignaturas += codigosInscripciones.size();
		}
		TimeUnit.MILLISECONDS.sleep(250);
    // Se crea una lista para los codigos de las asignaturas a omitir
    List<Integer> codigosAsignaturasOmitidas = new ArrayList<Integer>();
    int i = 0;
		while ( i == 0 ){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			int j = 0;
			while( j < 3 ){
				System.out.println("\nVuelta " + (j+1) );
				// Se revisa si se puede seleccionar el proceso de inscripciones
				driver.switchTo().defaultContent();
				driver.findElement(By.id("navbar-dropdown-procesos")).click();
				try {
					driver.findElement(By.linkText(Vars_LOA.inscripcionText)).click();
				} catch (Exception e) {
					System.out.println("No se encuentra el proceso de inscripcion");
					System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				// Se revisa si el proceso esta abierto
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				try {
					driver.switchTo().frame("derecho");
				} catch (Exception e2) {
					System.out.println("El proceso se encuentra cerrado");
					System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				driver.switchTo().defaultContent();
				TimeUnit.MILLISECONDS.sleep(250);
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				TimeUnit.MILLISECONDS.sleep(250);
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
				while( (l == 0) && (contadorAsignaturas <= CANTIDADASIGNATURASLIMITE) ) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
					TimeUnit.MILLISECONDS.sleep(250);
					listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
					for (WebElement linea : listadoAsignaturas2) {
						String codigo = linea.getText().split(" ")[0];
						String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
						listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
					}
					if( listadoCodigoNombre2.size() == 0 ){
						System.out.println("	No hay asignaturas disponibles para inscribir");
						l = 1;
					} else {
						Boolean existeTeoria = false;
						Boolean existeLaboratorio = false;
						Boolean existeEjercicio = false;
						Boolean seleccionoTeoria = false;
						Boolean seleccionoLaboratorio = false;
						Boolean seleccionoEjercicio = false;
						// Se crea el random para seleccionar una asignatura al azar
						Random rand = new Random();
						int rand_int1 = rand.nextInt((listadoCodigoNombre2.size() - 1) + 1) + 1;
						// Se revisa cuantas asignaturas se omitieron
						int asignaturasOmitidas = 0;
						// Mientras el while este en la lista de omitidos y no se alcance el limite, se busca otro
						while( (codigosAsignaturasAOmitir2.contains(Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0)))) && (asignaturasOmitidas <= listadoCodigoNombre2.size() ) ){
							rand_int1 = rand.nextInt((listadoCodigoNombre2.size() - 1) + 1) + 1;
							asignaturasOmitidas++;
						}
						// Si se revisaron todas las asignaturas en una vuelta, sale de la vuelta
						if( asignaturasOmitidas > listadoCodigoNombre2.size() ){
							l = 1;
						} else {
							// Se revisa si se puede seleccionar el proceso de inscripciones
							driver.switchTo().defaultContent();
							driver.findElement(By.id("navbar-dropdown-procesos")).click();
							try {
								driver.findElement(By.linkText(Vars_LOA.inscripcionText)).click();
							} catch (Exception e) {
								System.out.println("No se encuentra el proceso de inscripcion");
								System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se revisa si el proceso esta abierto
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							try {
								driver.switchTo().frame("derecho");
							} catch (Exception e2) {
								System.out.println("El proceso se encuentra cerrado");
								System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							driver.switchTo().defaultContent();
							TimeUnit.MILLISECONDS.sleep(250);
							// Se entra al frame de los derechos
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("derecho");
							// Se busca una asignatura
							WebElement asignatura = driver.findElement(By.linkText(listadoCodigoNombre2.get(rand_int1-1).get(1)));
							// Se crea el JavascriptExecutor para hacer scroll
							JavascriptExecutor js = (JavascriptExecutor) driver;
							// Se obtiene la posicion del boton
							Point location = asignatura.getLocation();
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+location.getY()+")");
							// Se hace click en la asignatura
							asignatura.click();
							System.out.println("\n	Asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) );
							TimeUnit.MILLISECONDS.sleep(250);
							// Se sale del frame, y se entra al frame de los cursos de teoria.
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_cteo");
							// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
							driver.switchTo().frame("mainFrame");
							System.out.println("	Se revisan los cursos de teoria");
							// Se obtienen todos los cursos
							List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
							TimeUnit.MILLISECONDS.sleep(175);
							// Se almacenan todas las secciones
							ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
							for (WebElement e : listadoTeoria) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_LOA.sinCoordinacionInscribirTeoriaText) ){
									// No hay cursos de laboratorio
									System.out.println("		No hay cursos de teoria");
								} else {
									listadoTeoriaAux.add(tmp);
									existeTeoria = true;
								}
							}
							// Se prueba seleccionar una seccion de teoria
							for ( int k = 0; k < listadoTeoriaAux.size(); k++ ) {
								String seccion = listadoTeoriaAux.get(k).replace("\n", " "	).strip();
								String[] seccionAux = seccion.split(" ");
								int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
								System.out.println("		Seccion " + (k+1) + " | " + seccion);
								System.out.println("		Seccion " + (k+1) + " | Cupos: " + cupos);
								// Se revisa si existe cupo
								if( cupos > 0 ){
									// Si hay cupos, se hace scroll y luego se selecciona
									WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(k+1)+") > td:nth-child(1)"));
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
									// Se selecciona la seccion
									seccionElement.click();
									seleccionoTeoria = true;
									System.out.println("		Seccion " + (k+1) + " | Seleccionada");
									break;
								} else {
									// Si no hay cupos, se selecciona el siguiente curso
									System.out.println("		Seccion " + (k+1) + " | Sin cupos");
								}
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
							TimeUnit.MILLISECONDS.sleep(250);
							// Se almacenan todas las secciones
							ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
							for (WebElement e : listadoLaboratorio) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_LOA.sinCoordinacionInscribirLabText) ){
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
							// Se sale del frame, y se entra al frame de los cursos de ejercicios.
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_ceje");
							// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
							driver.switchTo().frame("mainFrame");
							System.out.println("	Se revisan los cursos de ejercicio");
							// Se obtienen todos los cursos
							List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
							TimeUnit.MILLISECONDS.sleep(175);
							// Se almacenan todas las secciones
							ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
							for (WebElement e : listadoEjercicio) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_LOA.sinCoordinacionInscribirEjeText) ){
									// No hay cursos de laboratorio
									System.out.println("		No hay cursos de ejercicios");
								} else {
									listadoEjercicioAux.add(tmp);
									existeEjercicio = true;
								}
							}
							// Se prueba seleccionar una seccion de ejercicio
							for ( int k = 0; k < listadoEjercicioAux.size(); k++ ) {
								String seccion = listadoEjercicioAux.get(k).replace("\n", " "	).strip();
								String[] seccionAux = seccion.split(" ");
								int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
								System.out.println("		Seccion " + (k+1) + " | " + seccion);
								System.out.println("		Seccion " + (k+1) + " | Cupos: " + cupos);
								// Se revisa si existe cupo
								if( cupos > 0 ){
									// Si hay cupos, se hace scroll y luego se selecciona
									WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(k+1)+") > td:nth-child(1)"));
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
									// Se selecciona la seccion
									seccionElement.click();
									seleccionoEjercicio = true;
									System.out.println("		Seccion " + (k+1) + " | Seleccionada");
									break;
								} else {
									// Si no hay cupos, se selecciona el siguiente curso
									System.out.println("		Seccion " + (k+1) + " | Sin cupos");
								}
							}
							// Se revisa que exista al menos un curso y se haya podido seleccionar
							if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
								// Se puede postula a la asignatura
								driver.switchTo().defaultContent();
								driver.switchTo().frame("mainFrame");
								driver.switchTo().frame(5);
								// Se postula a la asignatura
								driver.findElement(By.id("btn_inscribir")).click();
								TimeUnit.MILLISECONDS.sleep(250);
								if (driver.switchTo().alert().getText().compareTo(Vars_LOA.alertaInscribirText) == 0 ) {
									driver.switchTo().alert().accept();
									TimeUnit.MILLISECONDS.sleep(250);
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame(5);
									// Se revisa el mensaje
									List<WebElement> mensajes = driver.findElements(By.cssSelector(".col-12 > div"));
									TimeUnit.MILLISECONDS.sleep(250);
									for (WebElement mensaje : mensajes){
										TimeUnit.MILLISECONDS.sleep(250);
										// Se revisa si el mensaje corresponde a asignatura inscrita
										if( mensaje.getText().compareTo(Vars_LOA.inscripcionStatusInscritaText + Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0)) + " (" + Vars_LOA.proceso + ")." ) == 0 ) {
											System.out.println("	Asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) + " inscrita." );
											// Se guarda la asignatura inscrita
											int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
											codigosAsignaturasInscritos.add(codigo);
											contadorAsignaturas++;
										} else {
											System.out.println("	Asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) + " NO inscrita." );
                      // Se guarda el codigo para intentar mandarlo por solicitud
                      int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
											if( !codigosAsignaturasOmitidas.contains(codigo) ){
												codigosAsignaturasOmitidas.add(codigo);
											}
										}
									}
								} else {
									System.out.println("No coincide el mensaje al inscribir la asignatura");
								}
								// Tiempo para guardar la asignatura postulada
								TimeUnit.MILLISECONDS.sleep(250);
							} else {
                System.out.println("	No es posible inscribir la asignatura: " + listadoCodigoNombre2.get(rand_int1-1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1-1).get(1) );
                // Se guarda el codigo para intentar mandarlo por solicitud
                int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
                if( !codigosAsignaturasOmitidas.contains(codigo) ){
									codigosAsignaturasOmitidas.add(codigo);
								}
							}
							// Se guarda el random para saber que esta asignatura no se debe repetir
							int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0));
							codigosAsignaturasAOmitir2.add(codigo);
						}
					}
				}
				j++;
			}
			i = 1;
			System.out.println("\n	Se inscribieron " + contadorAsignaturas + " asignaturas");
			
			int k = 0;
			while( k < codigosAsignaturasOmitidas.size() ){
				if( codigosAsignaturasInscritos.contains(codigosAsignaturasOmitidas.get(k)) ){
					codigosAsignaturasOmitidas.remove(k);
				}
				k++;
			}
      // Si no se llego al limite de asignaturas se mandan solicitudes
      if( contadorAsignaturas <= CANTIDADASIGNATURASLIMITE ){
				// Se intenta mandar solicitud
        System.out.println("	Se intenta mandar solicitud para las asignaturas omitidas: ");
				for ( int codigoAsignatura : codigosAsignaturasOmitidas ){
					System.out.println("		" + codigoAsignatura);
				}
				int asignaturasPorMandarSolicitud = codigosAsignaturasOmitidas.size();
        // Se revisa si se puede seleccionar el proceso de postulaciones
        driver.switchTo().defaultContent();
        driver.findElement(By.id("navbar-dropdown-procesos")).click();
        try {
          driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
        } catch (Exception e) {
          System.out.println("No se encuentra el proceso de solicitudes");
          System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
          driver.quit();
          return;
        }
        // Se revisa si el proceso esta abierto
        driver.switchTo().defaultContent();
        driver.switchTo().frame("mainFrame");
        try {
          driver.switchTo().frame("derecho");
        } catch (Exception e2) {
          System.out.println("El proceso de solicitudes se encuentra cerrado");
          System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
          driver.quit();
          return;
        }
        // Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				TimeUnit.MILLISECONDS.sleep(250);
				ArrayList<ArrayList<String>> listadoCodigoNombre = new ArrayList<ArrayList<String>>();
				for (WebElement linea : listadoAsignaturas) {
					String codigo = linea.getText().split(" ")[0];
					String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
					listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}
				// Se revisan todos los cursos
				int l = 0;
				while( (l == 0) && (contadorAsignaturas <= CANTIDADASIGNATURASLIMITE) && (asignaturasPorMandarSolicitud > 0) ) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
					TimeUnit.MILLISECONDS.sleep(250);
					listadoCodigoNombre = new ArrayList<ArrayList<String>>();
					for (WebElement linea : listadoAsignaturas) {
						String codigo = linea.getText().split(" ")[0];
						String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
						listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
					}
					if( listadoCodigoNombre.size() == 0 ){
						System.out.println("	No hay asignaturas disponibles para inscribirse");
						l = 1;
					} else {
						int m = 0;
						while( m < asignaturasPorMandarSolicitud ){
							// Se revisa si se puede seleccionar el proceso de postulaciones
							driver.switchTo().defaultContent();
							driver.findElement(By.id("navbar-dropdown-procesos")).click();
							try {
								driver.findElement(By.linkText(Vars_LOA.solicitudText)).click();
							} catch (Exception e) {
								System.out.println("No se encuentra el proceso de solicitudes");
								System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se revisa si el proceso esta abierto
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							try {
								driver.switchTo().frame("derecho");
							} catch (Exception e2) {
								System.out.println("El proceso de solicitudes se encuentra cerrado");
								System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se obtienen todas las asignaturas
							List<WebElement> listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
							TimeUnit.MILLISECONDS.sleep(250);
							ArrayList<ArrayList<String>> listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
							for (WebElement linea : listadoAsignaturas2) {
								String codigo = linea.getText().split(" ")[0];
								String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
								listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
							}
							int o = 0;
							System.out.println("\n	Se intenta mandar solicitud para la asignatura: " + codigosAsignaturasOmitidas.get(m));
							while ( o < listadoCodigoNombre2.size() ){
								if( Integer.parseInt(listadoCodigoNombre2.get(o).get(0)) == codigosAsignaturasOmitidas.get(m) ){
									// Se busca la asignatura
									WebElement asignatura = driver.findElement(By.cssSelector("tr.bold7:nth-child("+(o+1)+") > td:nth-child(2) > a:nth-child(1)"));
									// Se crea el JavascriptExecutor para hacer scroll
									JavascriptExecutor js = (JavascriptExecutor) driver;
									// Se obtiene la posicion del boton
									Point location = asignatura.getLocation();
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+location.getY()+")");
									// Se hace click en la asignatura
									asignatura.click();
									TimeUnit.MILLISECONDS.sleep(250);
									Boolean existeTeoria = false;
									Boolean existeLaboratorio = false;
									Boolean existeEjercicio = false;
									Boolean seleccionoTeoria = false;
									Boolean seleccionoLaboratorio = false;
									Boolean seleccionoEjercicio = false;
									Random rand = new Random();
									// Se sale del frame, y se entra al frame de los cursos de teoria.
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame("frame_cteo");
									// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
									driver.switchTo().frame("mainFrame");
									System.out.println("		Se revisan los cursos de teoria");
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtienen todos los cursos
									List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtiene la primera linea despues de buscar tr
									String primeraLineaTeoria = listadoTeoria.get(0).getText().strip();
									// Si la primera linea dice que no hay coordinaciones, se avisa
									if( !primeraLineaTeoria.equals(Vars_LOA.sinCoordinacionSolicitudTeoriaText) ){
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
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtienen todos los cursos
									List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtiene la primera linea despues de buscar tr
									String primeraLineaEje = listadoEjercicio.get(0).getText().strip();
									// Si la primera linea dice que no hay coordinaciones, se avisa
									if( !primeraLineaEje.equals(Vars_LOA.sinCoordinacionSolicitudEjeText) ){
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
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtienen todos los cursos
									List<WebElement> listadoLab = driver.findElements(By.cssSelector("tr"));
									TimeUnit.MILLISECONDS.sleep(250);
									// Se obtiene la primera linea despues de buscar tr
									String primeraLineaLab = listadoLab.get(0).getText().strip();
									// Si la primera linea dice que no hay coordinaciones, se avisa
									if( !primeraLineaLab.equals(Vars_LOA.sinCoordinacionSolicitudLabText) ){
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
										System.out.println("	Se solicitó la asignatura");
										// Se sale del frame, y se entra al frame deonde esta el boton de postular
										driver.switchTo().defaultContent();
										driver.switchTo().frame("mainFrame");
										driver.switchTo().frame("frame_solicitudes");
										// Se ingresa el motivo de la postulacion
										driver.findElement(By.id("motivo")).click();
										driver.findElement(By.id("motivo")).sendKeys("Motivo Test");
										// Se postula a la asignatura
										driver.findElement(By.id("btn_solicitar")).click();
										TimeUnit.MILLISECONDS.sleep(250);
										if (driver.switchTo().alert().getText().compareTo(Vars_LOA.alertaSolicitudConfirmText) == 0 ) {
											driver.switchTo().alert().accept();
										}
										contadorAsignaturas++;
										// Tiempo para guardar la asignatura postulada
										TimeUnit.MILLISECONDS.sleep(250);
									} else {
										// No se puede postular a la asignatura
										System.out.println("	No se puedó solicitar la asignatura");
										driver.switchTo().defaultContent();
										driver.switchTo().frame("mainFrame");
										driver.switchTo().frame("derecho");
									}
									o = listadoCodigoNombre.size();
									m++;
								} else {
									o++;
								}
							}
						}
					}
				}
      } else {
        System.out.println("    Se llego al limite de asignaturas");
      }
		}
		System.out.println("Se finaliza el test Test_2_Inscribir_Solicitud_Limite\n\n==========================================================\n");
		driver.quit();
	}
}