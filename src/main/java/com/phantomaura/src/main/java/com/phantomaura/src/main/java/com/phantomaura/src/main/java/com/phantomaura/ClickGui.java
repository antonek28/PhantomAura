
package com.phantomaura;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGui extends Screen {

    private enum Tab {
        COMBAT, VISUALS, SETTINGS
    }

    private Tab tab = Tab.COMBAT;
    private String dragging = "";

    private static final int BG = 0xFF15121D;
    private static final int PANEL = 0xFF292136;
    private static final int PURPLE = 0xFFAC62FF;
    private static final int WHITE = 0xFFFFFFFF;

    public ClickGui() {
        super(Text.literal("PhantomAura"));
    }

    private int left() {
        return width / 2 - 145;
    }

    private int top() {
        return height / 2 - 175;
    }

    @Override
    public void render(
        DrawContext ctx, int mx, int my, float delta
    ) {
        renderBackground(ctx, mx, my, delta);

        int x = left();
        int y = top();

        ctx.fill(x, y, x + 290, y + 350, BG);
        ctx.fill(x, y, x + 290, y + 35, PURPLE);

        ctx.drawText(
            textRenderer, "PHANTOMAURA",
            x + 12, y + 12, WHITE, false
        );

        drawTab(ctx, x + 10, y + 48, "COMBAT", tab == Tab.COMBAT);
        drawTab(ctx, x + 105, y + 48, "VISUALS", tab == Tab.VISUALS);
        drawTab(ctx, x + 200, y + 48, "SETTINGS", tab == Tab.SETTINGS);

        if (tab == Tab.COMBAT) {
            drawToggle(ctx, x, y + 90, "Auto Target", AuraSettings.autoTarget);
            drawToggle(ctx, x, y + 122, "Hostile Mobs", AuraSettings.hostileMobs);
            drawToggle(ctx, x, y + 154, "Passive Mobs", AuraSettings.passiveMobs);
            drawToggle(ctx, x, y + 186, "Players", AuraSettings.players);
            drawSlider(ctx, x, y + 240, "Range", AuraSettings.range, 1, 6);
        }

        if (tab == Tab.VISUALS) {
            drawToggle(ctx, x, y + 90, "Target HUD", AuraSettings.targetESP);
        }

        if (tab == Tab.SETTINGS) {
            ctx.drawText(textRenderer, "CONFIGURATION", x + 15, y + 95, PURPLE, false);
            drawButton(ctx, x, y + 125, "SAVE CONFIG");
            drawButton(ctx, x, y + 165, "RESET CONFIG");
        }

        super.render(ctx, mx, my, delta);
    }

    private void drawTab(DrawContext ctx, int x, int y, String label, boolean active) {
        ctx.fill(x, y, x + 80, y + 24, active ? PURPLE : PANEL);
        ctx.drawText(textRenderer, label, x + 8, y + 8, WHITE, false);
    }

    private void drawToggle(DrawContext ctx, int x, int y, String label, boolean enabled) {
        ctx.fill(x + 10, y, x + 280, y + 25, PANEL);
        ctx.drawText(textRenderer, label, x + 18, y + 8, WHITE, false);
        ctx.fill(x + 245, y + 5, x + 270, y + 20,
            enabled ? PURPLE : 0xFF555555);
    }

    private void drawButton(DrawContext ctx, int x, int y, String label) {
        ctx.fill(x + 10, y, x + 280, y + 28, PANEL);
        ctx.drawText(textRenderer, label, x + 18, y + 10, WHITE, false);
    }

    private void drawSlider(
        DrawContext ctx, int x, int y,
        String label, double value, double min, double max
    ) {
        ctx.drawText(
            textRenderer, label + ": " + String.format("%.1f", value),
            x + 15, y, WHITE, false
        );

        int bx = x + 15;
        int by = y + 19;
        int barWidth = 255;

        ctx.fill(bx, by, bx + barWidth, by + 6, PANEL);

        int filled = (int)(barWidth * (value - min) / (max - min));

        ctx.fill(bx, by, bx + filled, by + 6, PURPLE);
    }

    private void toggleAt(int pos) {
        if (tab == Tab.COMBAT) {
            if (pos == 90) AuraSettings.autoTarget = !AuraSettings.autoTarget;
            if (pos == 122) AuraSettings.hostileMobs = !AuraSettings.hostileMobs;
            if (pos == 154) AuraSettings.passiveMobs = !AuraSettings.passiveMobs;
            if (pos == 186) AuraSettings.players = !AuraSettings.players;
        } else if (tab == Tab.VISUALS && pos == 90) {
            AuraSettings.targetESP = !AuraSettings.targetESP;
        }

        ConfigManager.save();
    }

    private void setSlider(double mx) {
        double progress = Math.max(0, Math.min(
            1, (mx - (left() + 15)) / 255.0
        ));

        AuraSettings.range = Math.round((1 + progress * 5) * 10) / 10.0;
        ConfigManager.save();
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return super.mouseClicked(mx, my, button);

        int x = left();
        int y = top();

        if (my >= y + 48 && my <= y + 72) {
            if (mx >= x + 10 && mx <= x + 90) tab = Tab.COMBAT;
            else if (mx >= x + 105 && mx <= x + 185) tab = Tab.VISUALS;
            else if (mx >= x + 200 && mx <= x + 280) tab = Tab.SETTINGS;
            return true;
        }

        if (tab == Tab.COMBAT || tab == Tab.VISUALS) {
            int[] positions = tab == Tab.COMBAT
                ? new int[]{90, 122, 154, 186}
                : new int[]{90};

            for (int pos : positions) {
                if (mx >= x + 10 && mx <= x + 280 &&
                    my >= y + pos && my <= y + pos + 25) {
                    toggleAt(pos);
                    return true;
                }
            }
        }

        if (tab == Tab.COMBAT && my >= y + 259 && my <= y + 271) {
            dragging = "range";
            setSlider(mx);
            return true;
        }

        if (tab == Tab.SETTINGS) {
            if (my >= y + 125 && my <= y + 153) {
                ConfigManager.save();
                return true;
            }

            if (my >= y + 165 && my <= y + 193) {
                AuraSettings.reset();
                ConfigManager.save();
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean mouseDragged(
        double mx, double my, int button, double dx, double dy
    ) {
        if (button == 0 && dragging.equals("range")) {
            setSlider(mx);
            return true;
        }

        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragging = "";
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        ConfigManager.save();
        if (client != null) client.setScreen(null);
    }
}
