package com.amadornes.tbircme.client.render;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {
    
    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent event) {
    
//        if (event.type == ElementType.CHAT) {
//            
//            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
//            
//            GL11.glPushMatrix();
//            {
//                int amt = 0;
//                GL11.glTranslated(320, event.resolution.getScaledHeight_double() - 36, 0);
//                Iterator<ChatLine> iterator = IRCEventHandler.messages.iterator();
//                while (iterator.hasNext()) {
//                    ChatLine l = iterator.next();
//                    IChatComponent icc = l.func_151461_a();
//                    if (icc instanceof ChatComponentTranslation) {
//                        ChatComponentTranslation cct1 = (ChatComponentTranslation) icc;
//                        if (cct1.getFormatArgs()[1] instanceof ChatComponentText) {
//                            ChatComponentText cct = (ChatComponentText) cct1.getFormatArgs()[1];
//                            int lines = 0;
//                            int len = fr.getStringWidth(((IChatComponent) cct1.getFormatArgs()[0]).getUnformattedTextForChat());
//                            List siblings = cct.getSiblings();
//                            for (Object o : siblings) {
//                                if (o instanceof ChatComponentEmote) {
//                                    RenderHelper.drawColoredRect(0, 0, 10, 10, 0xFF0000);
//                                    System.out.println(++amt);
//                                }
//                            }
//                        }
//                    }
//                    GL11.glTranslated(0, -Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, 0);
//                }
//            }
//            GL11.glPopMatrix();
//        }
    }
    
}
