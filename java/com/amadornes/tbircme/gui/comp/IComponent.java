package com.amadornes.tbircme.gui.comp;

public interface IComponent {
    
    void render(int x, int y, float frame);
    
    public void onClick(int x, int y, int button);
    
    public void onMouseDrag(int x, int y, int button);
    
    void onMouseUp(int x, int y, int button);
    
    public void onKeyType(char c, int key);
    
    public int getX();
    
    public int getY();
    
    public int getWidth();
    
    public int getHeight();
    
}
