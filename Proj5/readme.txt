Authors: Omaid Khan & Jesus Solorzano

Note: Implemented both extra credit options.
	+5pts - Holding down the bottom arrow makes 
			the piece go down hard
		  - Tap the piece allows for the piece 
			to move down slower and still return
			to normal speeds
	+10pts - MenuBar has a button that switched gravity
			from Flood Fill to Naive Gravity. 
			

Design Patterns Used: 
--- Observer Pattern---
	- The Following piece of code shows how the application is implemented 
	to constantly check for user input from the keyboard. Which allows
	the application to perform some functionality such as move left, right,
	or rotate. This Key Listener is added to the JFrame which serves as the GUI.
	--- this.addKeyListener(new KeyListener() {
            boolean rest = false;

            @Override
            public void keyTyped(KeyEvent e) {


            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // DOWN-----------------------------
                    case KeyEvent.VK_DOWN:
                        System.out.println("down");
                        if (!rest) {
                            if (timer.actual_timer.isRunning())
                                timer.actual_timer.stop();
                            if (!curPiece.moveDown()) {
                                timer.actual_timer.restart();
                                rest = true;
                            }
                        }
                        break;
                    // DOWN END---------------------------
                    // RIGHT------------------------------
                    case KeyEvent.VK_RIGHT:
                        System.out.println("right");
                        curPiece.moveRight();
//                        if (timer.actual_timer.isRunning())
//                            timer.actual_timer.stop();
//                        if (!curPiece.moveRight()) {
//                            timer.actual_timer.restart();
//                        }
                        break;
                    // RIGHT END--------------------------
                    // LEFT-------------------------------
                    case KeyEvent.VK_LEFT:
                        System.out.println("left");
                        curPiece.moveLeft();
                        break;
                    // LEFT END---------------------------
                    // UP---------------------------------
                    case KeyEvent.VK_UP:
                        System.out.println("up");
                        if (timer.actual_timer.isRunning())
                            timer.actual_timer.stop();
                        curPiece.rotate();
                        timer.actual_timer.restart();
                        break;
                    // UP END-----------------------------
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("up");
                    timer.actual_timer.restart();
                    rest = false;
                }

            }
        });
		
	--- end of code