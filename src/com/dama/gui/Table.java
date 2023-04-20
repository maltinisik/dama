package com.dama.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.dama.engine.board.Board;
import com.dama.engine.board.BoardUtils;
import com.dama.engine.board.Move;
import com.dama.engine.board.Move.AttackMove;
import com.dama.engine.board.Tile;
import com.dama.engine.pieces.Piece;
import com.dama.engine.player.MoveTransition;
import com.google.common.collect.Lists;

public class Table extends Observable {
  
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board board;
	private final GameSetup gameSetup;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	private static final Color darkColor  = new Color(204,102,0); //Color.decode("#593E1A"); //new Color(204,102,0);
	private static final Color lightColor = new Color(255,204,153); //Color.decode("#FFFACD"); //new Color(255,204,153);
	private static final String images_path = "Images/";
	
	private Tile sourceTile = null;
	private Tile destinationTile = null;
	private Piece humanMovedPiece = null;
	private List<Integer> attackedMoves= new ArrayList<Integer>();
	private BoardDirection boardDirection;
	private Move updateComputerMove;
	
	private static final Table INSTANCE = new Table();
	
	public static Table get() {
		return INSTANCE;
	}
	
	private Table() {
	  this.gameFrame = new JFrame("Dama");
	  this.gameFrame.setLocation(400, 100);
	  final JMenuBar tablemenuBar = createTableMenuBar();
	  this.gameFrame.setJMenuBar(tablemenuBar);
	  this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
	  this.board = Board.createStandardBoard();
	  this.boardPanel=new BoardPanel();
	  this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
	  this.gameFrame.setVisible(true);
	  this.gameFrame.setDefaultCloseOperation(3);
	  this.boardDirection = BoardDirection.NORMAL;
	  this.gameSetup = new GameSetup(this.gameFrame, true);
	}
	
	private GameSetup getGameSetup() {
		return this.gameSetup;
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tablemenuBar = new JMenuBar();
		tablemenuBar.add(CreateFileMenu());
		tablemenuBar.add(CreatePreferencesMenu());
		tablemenuBar.add(CreateOptionsMenu());
		
		return tablemenuBar;
	}

	private JMenu CreateFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open up the pgn file");
			}
		});
		
		fileMenu.add(openPGN);
		
		//Exit button
		final JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
               System.exit(0);
			}
		});
		
		fileMenu.add(exit);
		
		return fileMenu;
	}
	
	private JMenu CreatePreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		
		final JMenuItem flipTheBoard = new JMenuItem("Flip The Board");
		flipTheBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection=boardDirection.opposite();
				boardPanel.drawBoard(board);
			}
		});
		
		preferencesMenu.add(flipTheBoard);
		
		return preferencesMenu;
	}
	
	private JMenu CreateOptionsMenu() {
		final JMenu optionsMenu = new JMenu("Options");
		
		final JMenuItem setupGameMenuItem = new JMenuItem("Setup");
		setupGameMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
              Table.get().getGameSetup().promptUser();
              Table.get().setupUpdate(Table.get().getGameSetup());
			}
		});
		
		optionsMenu.add(setupGameMenuItem);
		
		return optionsMenu;		
	}
	
	private void setupUpdate(GameSetup gameSetup) {
		setChanged();
		notifyObservers(gameSetup);
	}
	
    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }
	
	public enum BoardDirection {
		NORMAL {
			@Override
			public BoardDirection opposite() {
				return FLIPPED;
			}

			@Override
			public List<TilePanel> traverse(List<TilePanel> listTilePanel) {
				return listTilePanel;
			}
		},
		FLIPPED {
			@Override
			public BoardDirection opposite() {
				return NORMAL;
			}

			@Override
			public List<TilePanel> traverse(List<TilePanel> listTilePanel) {
				return Lists.reverse(listTilePanel);
			}
		};
		
		public abstract BoardDirection opposite();
		
		public abstract java.util.List<TilePanel> traverse(java.util.List<TilePanel> listTilePanel);
	}
	
	private class BoardPanel extends JPanel {
		final java.util.List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			
			for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(Board board) {
          removeAll();
          for (TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
        	  tilePanel.drawTile(board);
        	  add(tilePanel);
        	  validate();
        	  repaint();
		  }
		}
	}
	
	enum PlayerType {
		HUMAN,
		COMPUTER;
	}
	
	private class TilePanel extends JPanel {

		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel,
				  final int tileId) {
			super(new GridLayout());
			this.tileId=tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(board);
		
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(final MouseEvent e) {
					//System.out.println("mouseReleased");
				}
				
				@Override
				public void mousePressed(final MouseEvent e) {
					//System.out.println("mousePressed");
				}
				
				@Override
				public void mouseExited(final MouseEvent e) {
					//System.out.println("mouseExited");
				}
				
				@Override
				public void mouseEntered(final MouseEvent e) {
					System.out.println("postion: " + tileId);
				}
				
				@Override
				public void mouseClicked(final MouseEvent e) {
			          //left click
					  if(SwingUtilities.isLeftMouseButton(e)) {
			        	  //first click
						  if (sourceTile==null) {
							sourceTile = board.getTile(tileId);
				            humanMovedPiece = sourceTile.getPiece();
				            if (humanMovedPiece==null || (humanMovedPiece.getPieceAlliance()!=board.getCurrentPlayer().getAlliance())) {
								sourceTile = null;
							}
				            else {
					            Collection<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();
					            if (!legalMoves.isEmpty()) {
					            	attackedMoves= new ArrayList<Integer>();
						            for (Move legalMove : legalMoves) {
						            	if (legalMove.getMovedPiece()==humanMovedPiece) {
						            		//if(checkMoveIsValid(legalMove)) {  //satranc icin gerekli olan bu hamle dama icin gerekli olmayabildigi icin kaldirildi
						            			if(legalMove.isAttack()) System.out.println("Attack count="+((AttackMove)legalMove).getCountOfCapturedPiecesWithAttackMoves());
							            		attackedMoves.add(legalMove.getDestinationCoordinate());						            			
						            		//}
										}
									}
								}
				            
							    invokeLater(boardPanel);
				            }
						  }
						  //second click
			        	  else {
			        		  destinationTile = board.getTile(tileId);
			        		  final Move move = Move.MoveFactory.createMove(board, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
			        	      MoveTransition transition = board.getCurrentPlayer().makeMove(move);
			        	      if (transition.getMoveStatus().isDone()) {
								board = transition.getTransitionBoard();
							  }
				              clearTiles();
			        	  }
						  SwingUtilities.invokeLater(new Runnable() {
								 @Override
								 public void run() {
				                     if (gameSetup.isAIPlayer(board.getCurrentPlayer())) {
			                            Table.get().moveMadeUpdate(PlayerType.HUMAN);
			                         }
									 boardPanel.drawBoard(board);
								 }
							  });
						  
			          }
					  
			          //right click
			          if(SwingUtilities.isRightMouseButton(e)) {
			            	clearTiles();
			          }
				}

				private boolean checkMoveIsValid(Move move) {
	        	    MoveTransition transition = board.getCurrentPlayer().makeMove(move);
	        	    if (transition.getMoveStatus().isDone()) {
						return true;
					}
					
					return false;
				}

				private void invokeLater(final BoardPanel boardPanel) {
					SwingUtilities.invokeLater(new Runnable() {
						 @Override
						 public void run() {
							 boardPanel.drawBoard(board);
						 }
					  });
				}

				private void clearTiles() {
					attackedMoves= new ArrayList<Integer>();
					sourceTile = null;
					destinationTile = null;
					humanMovedPiece = null;
					
					invokeLater(boardPanel);
				}
			});
			
			validate();
		}

		public void drawTile(final Board board) {
           assignTileColor();
           assignTilePieceIcon(board);
           assignAttackedTileIcon(board);
           validate();
           repaint();
		}

		private void assignAttackedTileIcon(final Board board) {
			String file;   
			if (attackedMoves.contains(tileId)) {
		           this.removeAll();
		           if (!board.getTile(tileId).isTileOccupied()) {
			           file = images_path+"Solid_green_10.svg.png";
				   }
		           else {
			           file = images_path+"A"+board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1)+board.getTile(tileId).getPiece().toString()+".png";;
		           }

	        	   try {
	    			final BufferedImage image = ImageIO.read(new File(file));
	    			JLabel jLabel = new JLabel(new ImageIcon(image));
	    			add(jLabel);
	    			this.revalidate();
	    		   } catch (IOException e) {
	    			e.printStackTrace();
	    		   }
			   }
		}
		
		private void assignTilePieceIcon(final Board board) {
           this.removeAll();
           if (board.getTile(tileId).isTileOccupied()) {
               String file = images_path+board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1)+board.getTile(tileId).getPiece().toString()+".png";
        	   try {
    			final BufferedImage image = ImageIO.read(new File(file));
    			add(new JLabel(new ImageIcon(image)));
    			this.revalidate();
    		   } catch (IOException e) {
    			e.printStackTrace();
    		   }
		   }
		}

		private void assignTileColor() {
			if (BoardUtils.FIRST_ROW[this.tileId] || 
					BoardUtils.THIRD_ROW[this.tileId] || 
					BoardUtils.FIFTH_ROW[this.tileId] || 
					BoardUtils.SEVENTH_ROW[this.tileId]) {
				setBackground((this.tileId % 2) == 0 ? lightColor : darkColor);
			} else {
				setBackground((this.tileId % 2) == 0 ? darkColor : lightColor);
			}
		}
	}

	public Board getGameBoard() {
		return this.board;
	}

	public void updateComputerMove(Move move) {
		this.updateComputerMove=move;
	}

	public void updateGameBoard(Board board) {
		this.board=board;
	}

	public BoardPanel getBoardPanel() {
		return this.boardPanel;
	}

    public void show() {
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

}