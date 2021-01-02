
	$(document).ready(function () {
			/********************************** DOC READY ***********************/
			//CARGA EL SONIDO
			//***************************************************************
			lowLag.init({
				sm2url: '/Scripts/sm2/swf/',
				urlPrefix: 'http://38.65.156.253:9080/sounds/'
			});

			lowLag.load(['mixdown.mp3', 'mixdown.ogg', 'mixdown.aif'], 'mixdown');

 
			//SETEA VARIABLES INICIALES
			//***************************************************************
			var windowWidth = '';
			var windowHeight = '';
			var rodilloFin = '';
			var rodilloFinLeft1 = '';
			var rodilloFinRight1 = '';
			var roboArmsTop = '';
			var leftArmRight = '';
			var rightArmLeft = '';
			var actualState = 'start';

			function setVars() {
				windowWidth = $(window).width();
				windowHeight = $(window).height();
				if (windowWidth <= 640) {
					rodilloFin = ((windowHeight * 0.50) + (windowWidth * 0.9)) + 'px';
					rodilloFinLeft1 = ((windowWidth * 0.50) - (windowWidth * 0.19)) + 'px';
					rodilloFinRight1 = ((windowWidth * 0.50) + (windowWidth * 0.19)) + 'px';
					roboArmsTop = (windowWidth * 0.5772619047619048) + 'px';
					leftArmRight = (windowWidth * 0.4101190476190476) + 'px';
					rightArmLeft = (windowWidth * 0.4132142857142857) + 'px';
				} else {
					rodilloFin = '122%';
					rodilloFinLeft1 = ((windowWidth * 0.50) - 95) + 'px';
					rodilloFinRight1 = ((windowWidth * 0.50) + 95) + 'px';
					roboArmsTop = '373px';
					leftArmRight = '265px';
					rightArmLeft = '267px';
				}
			}

			setVars();

			$(window).resize(function () {
				setVars();
			});

			//PRECARGA DE IMAGENES
	        	$(window).load(function () {
	        	       
	                if(screen.width>=360 && screen.width<380){
				    
				   
				    //document.body.addEventListener('touchmove', function(e){ e.preventDefault(); });
				    //document.html.addEventListener('touchmove', function(e){ e.preventDefault(); });
				}	        	    
	        	    
            window.scrollTo(0,0);
            console.log(screen.width)
				$('#animateCanvas').imagesLoaded(function () {
						
					TweenMax.to('#loading', 1.0, { opacity: "0", ease: Power2.easeInOut, delay: 0.5 });
					TweenMax.to('#loading', .1, { top: "-200%", ease: Power2.easeInOut, delay: 1.5 });
					TweenMax.to('#botonINI', 1, { opacity: "1", ease: Power2.easeInOut, delay: 2 });
					
				});
			})

			//FUNCION DE ANIMACION DE PIEZAS
			//***************************************************************
	
			
			function goPlayAnimation() {
			   	//ABRE COMPUERTAS ESPACIALES
				function changeText() { $('#botonINI').html("FINALIZAR"); actualState = 'download'; }
				//LETTERS APPEAR
			
                setTimeout(function(){ window.ondragstart = function() { return false; }  },   30000);
				setTimeout(function(){ TweenMax.staggerFrom('.nameHolder', 1, { opacity: 0, color: '#ef6b00', ease: Power2.easeOut, delay: 16.5 }, 0.1); },   1000);
				setTimeout(function(){ TweenMax.staggerFrom('.apesHolder', 1, { opacity: 0, color: '#ef6b00', ease: Power2.easeOut, delay: 16.5 }, 0.1); },   1400);
				setTimeout(function(){ TweenMax.staggerFrom('.expHolder', 1, { opacity: 0, color: '#ef6b00', ease: Power2.easeOut, delay: 16.5 }, 0.1); },   1800);
//
				TweenMax.to('#leftDoor', 0.7, { left: "-60%", ease: Power2.easeInOut, delay: 0.4 });
				TweenMax.to('#rightDoor', 0.7, { right: "-60%", ease: Power2.easeInOut, delay: 0.4 });
				TweenMax.to('#lockDoor', 0.5, { top: "-40%", ease: Power2.easeInOut, delay: 0.2 });
				TweenMax.to('#botonINI', 0.2, { opacity: "0", ease: Power2.easeInOut, onComplete: changeText });
				TweenMax.to('#botonINI', 0.2, { top: "-200%", ease: Power2.easeInOut, delay: 0.2 });
				if(screen.height>=760 && screen.height<800){
				     setTimeout(function(){ window.scrollTo(0,85) }, 2500);
                     setTimeout(function(){ window.scrollTo(0,0) },   30000);
				
				}
			
               

				//ABRE COMPUERTA PISO

				TweenMax.to("#credeHole", 1, { width: "59.609375vw", ease: Power2.easeInOut, delay: 1 });

				//ENTRA PLASTICO

				TweenMax.to("#credePlasticFondo", 1, { top: "0", ease: Power2.easeInOut, delay: 2 });
				TweenMax.to("#credeGancho", 0.3, { height: "9.921875vw", ease: Power2.easeInOut, delay: 2.6 });

				//ENTRA BRAZO Y PINTA CENTRO

				TweenMax.to("#paintArmHolder", 1, { top: "50%", ease: Power2.easeInOut, delay: 3 });
				TweenMax.to("#paintArm", 0.3, { maxWidth: "241px", maxHeight: "1084px", width: "37.7265625vw", height: "169.3828125vw", ease: Power2.easeInOut, delay: 3 });
				TweenMax.to("#paintArm", 0.3, { maxWidth: "220px", maxHeight: "986px", width: "34.296875vw", height: "153.984375vw", ease: Power2.easeInOut, delay: 3.7 });
				TweenMax.to("#paintArmHolder", 1.7, { top: rodilloFin, ease: Power2.easeInOut, delay: 4.3 });
				TweenMax.to("#fondoPart02", 1.7, {top: "0",height: "100%", ease: Power2.easeInOut, delay: 4.3 });

				// SUBE BRAZO Y PINTA IZQUIERDA

				TweenMax.to("#paintArmHolder", 1, { left: rodilloFinLeft1, top: "50%", ease: Power2.easeInOut, delay: 6 });
				TweenMax.to("#paintArm", 0.3, { maxWidth: "241px", maxHeight: "1084px", width: "37.7265625vw", height: "169.3828125vw", ease: Power2.easeInOut, delay: 6 });
				TweenMax.to("#paintArm", 0.3, { maxWidth: "220px", maxHeight: "986px", width: "34.296875vw", height: "153.984375vw", ease: Power2.easeInOut, delay: 6.7 });
				TweenMax.to("#paintArmHolder", 1.7, { top: rodilloFin, ease: Power2.easeInOut, delay: 7.3 });
				TweenMax.to("#fondoPart01", 1.7, { height: "100%", ease: Power2.easeInOut, delay: 7.3 });

				// SUBRE BRAZO Y PINTA DERECHA

				TweenMax.to("#paintArmHolder", 1, { left: rodilloFinRight1, top: "50%", ease: Power2.easeInOut, delay: 9 });
				TweenMax.to("#paintArm", 0.3, { maxWidth: "241px", maxHeight: "1084px", width: "37.7265625vw", height: "169.3828125vw", ease: Power2.easeInOut, delay: 9 });
				TweenMax.to("#paintArm", 0.3, { maxWidth: "220px", maxHeight: "986px", width: "34.296875vw", height: "153.984375vw", ease: Power2.easeInOut, delay: 9.7 });
				TweenMax.to("#paintArmHolder", 1.7, { top: rodilloFin, ease: Power2.easeInOut, delay: 10.3 });
				TweenMax.to("#fondoPart03", 1.7, { height: "100%", ease: Power2.easeInOut, delay: 10.3 });

				// SE VA BRAZO PINTOR

				TweenMax.to("#paintArmHolder", 1, { left: '50%', top: "150%", ease: Power2.easeInOut, delay: 12 });
				TweenMax.to("#paintArm", 0.5, { maxWidth: "241px", maxHeight: "1084px", width: "37.7265625vw", height: "169.3828125vw", ease: Power2.easeInOut, delay: 12 });

				//BRAZO LINEA

				TweenMax.to("#rightArmHolder", 1.5, { left: '50%', ease: Power2.easeInOut, delay: 12.5 });
				TweenMax.to("#lineHolder", 1.5, { left: '50%', ease: Power2.easeInOut, delay: 12.5 });
				TweenMax.to("#rightArmHolder", 1, { left: '120%', ease: Power2.easeInOut, delay: 14 });
				TweenMax.to("#rightArm", 1, { left: rightArmLeft, top: roboArmsTop, ease: Power2.easeInOut, delay: 14 });

				//BRAZO PLECA

				TweenMax.to("#leftArmHolder", 1.5, { left: '50%', ease: Power2.easeInOut, delay: 13.5 });
				TweenMax.to("#plecaHolder", 1.5, { left: '50%', ease: Power2.easeInOut, delay: 13.5 });
				TweenMax.to("#leftArmHolder", 1, { left: '-20%', ease: Power2.easeInOut, delay: 15 });
				TweenMax.to("#leftArm", 1, { right: leftArmRight, top: roboArmsTop, ease: Power2.easeInOut, delay: 15 });

				//FLASHING CARRERA

				TweenMax.fromTo('#carrera', .15, { opacity: 0 }, { opacity: 1, repeat: 8, yoyo: true, delay: 15.5 });


				//TweenMax.staggerFrom('.charAnim', 1, { opacity: 0, color: '#ef6b00', ease: Power2.easeOut, delay: 16.5 }, 0.1);

				//FOTO SCREEN

				TweenMax.to("#screenArmHolder", 1.6, { top: '50%', ease: Power2.easeInOut, delay: 19 });
				TweenMax.to("#screenArm", 0.2, { maxWidth: "264px", maxHeight: "1001px", width: "41.25vw", height: "156.375vw", ease: Power2.easeInOut, delay: 18.8 });
				TweenMax.to("#screenArm", 0.7, { maxWidth: "220px", maxHeight: "834px", width: "34.375vw", height: "130.3125vw", ease: Power2.easeInOut, delay: 19.8 });
				TweenMax.fromTo('#screenArm', .1, { maxWidth: "220px", maxHeight: "834px", width: "34.375vw", height: "130.3125vw" }, { maxWidth: "227px", maxHeight: "859px", width: "35.40625vw", height: "134.221875vw", repeat: 10, yoyo: true, delay: 21 });
				TweenMax.to("#fotoHolder", 1.2, { opacity: '1', ease: Power2.easeInOut, delay: 22 });
				TweenMax.to("#screenArmHolder", 1.5, { top: '-20%', ease: Power2.easeInOut, delay: 22 });


				//BRAZO COLUMNA DERECHA

				TweenMax.to("#leftArmHolder", 1, { left: '50%', ease: Power2.easeInOut, delay: 21.5 });
				TweenMax.to("#calen01Holder", 1, { left: '50%', ease: Power2.easeInOut, delay: 21.5 });
				TweenMax.to("#leftArmHolder", 0.75, { left: '-20%', ease: Power2.easeInOut, delay: 23 });

				//BRAZO COLUMNA IZQUIERDA

				TweenMax.to("#rightArmHolder", 1, { left: '50%', ease: Power2.easeInOut, delay: 22.5 });
				TweenMax.to("#calen02Holder", 1, { left: '50%', ease: Power2.easeInOut, delay: 22.5 });
				TweenMax.to("#rightArmHolder", 0.75, { left: '120%', ease: Power2.easeInOut, delay: 24 });

				//BRAZO COLUMNA DERECHA

				TweenMax.to("#leftArmHolder", 1, { left: '50%', ease: Power2.easeInOut, delay: 23.5 });
				TweenMax.to("#calen03Holder", 1, { left: '50%', ease: Power2.easeInOut, delay: 23.5 });
				TweenMax.to("#leftArmHolder", 0.75, { left: '-20%', ease: Power2.easeInOut, delay: 25 });

				//BRAZO COLUMNA IZQUIERDA


				//TweenMax.to("#rightArmHolder", 1, { left: '50%', ease: Power2.easeInOut, delay: 24.5 });
				//TweenMax.to("#calen04Holder", 1, { left: '50%', ease: Power2.easeInOut, delay: 24.5 });
				//TweenMax.to("#rightArmHolder", 0.75, { left: '120%', ease: Power2.easeInOut, delay: 26 });



				//SALE GANCHO
				TweenMax.to("#credeGancho", 1, { height: "0", ease: Power2.easeInOut, delay: 26 });

				//CIERRA SPACE HATCH
				TweenMax.to('#leftDoor', 1, { left: "0", ease: Power2.easeInOut, delay: 29.5 });
				TweenMax.to('#rightDoor', 1, { right: "0", ease: Power2.easeInOut, delay: 29.5 });

				TweenMax.to('#lockDoor', 0.8, { top: "50%", ease: Power2.easeInOut, delay: 30 });

				TweenMax.to('#botonINI', 0.2, { top: "50%", ease: Power2.easeInOut, delay: 31.6 });

				TweenMax.to('#botonINI', 0.5, { opacity: "1", ease: Power2.easeInOut, delay: 32 });
			}

	

			$("#botonINI").on("click", this, function () {
				if (actualState == 'download') {
					console.log("redirect")
                    let ipBonita = window.location.protocol + "//" + window.location.host + "";
                    let url = "";
					url = ipBonita + "/apps/administrativo/PruebaGenerarCredencial/";
					console.log(url)
					
					
					window.location.replace(url); 
				} else {
					lowLag.play("mixdown");

					goPlayAnimation();
				}
			});


			
		});
	