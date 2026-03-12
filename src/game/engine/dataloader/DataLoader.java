package game.engine.dataloader;

import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cards.EnergyStealCard;
import game.engine.cards.ShieldCard;
import game.engine.cards.StartOverCard;
import game.engine.cards.SwapperCard;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.exceptions.InvalidCSVFormat;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Schemer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataLoader {

    public static final String CARDS_FILE_NAME = "cards.csv";
    public static final String CELLS_FILE_NAME = "cells.csv";
    private static final String MONSTERS_FILE_NAME = "monsters.csv";

    public static ArrayList<Card> readCards() throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE_NAME));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            try {
                String cardType = parts[0].trim().toUpperCase();
                String name = parts[1].trim();
                String description = parts[2].trim();
                int rarity = Integer.parseInt(parts[3].trim());

                switch (cardType) {
                    case "SWAPPER":
                        cards.add(new SwapperCard(name, description, rarity));
                        break;
                    case "SHIELD":
                        cards.add(new ShieldCard(name, description, rarity));
                        break;
                    case "ENERGYSTEAL":
                        int energy = Integer.parseInt(parts[4].trim());
                        cards.add(new EnergyStealCard(name, description, rarity, energy));
                        break;
                    case "STARTOVER":
                        boolean lucky = Boolean.parseBoolean(parts[4].trim());
                        cards.add(new StartOverCard(name, description, rarity, lucky));
                        break;
                    case "CONFUSION":
                        int duration = Integer.parseInt(parts[4].trim());
                        cards.add(new ConfusionCard(name, description, rarity, duration));
                        break;
                    default:
                        throw new InvalidCSVFormat(line);
                }
            } catch (InvalidCSVFormat e) {
                br.close();
                throw e;
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                br.close();
                throw new InvalidCSVFormat(line);
            }
        }
        br.close();
        return cards;
    }

    public static ArrayList<Cell> readCells() throws IOException {
        ArrayList<Cell> cells = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(CELLS_FILE_NAME));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            try {
                switch (parts.length) {
                    case 3:
                        // DoorCell: name, role, energy
                        String name = parts[0].trim();
                        Role role = Role.valueOf(parts[1].trim().toUpperCase());
                        int energy = Integer.parseInt(parts[2].trim());
                        cells.add(new DoorCell(name, role, energy));
                        break;
                    case 2:
                        // TransportCell: name, effect
                        String name2 = parts[0].trim();
                        int effect = Integer.parseInt(parts[1].trim());
                        if (effect > 0) {
                            cells.add(new ConveyorBelt(name2, effect));
                        } else {
                            cells.add(new ContaminationSock(name2, effect));
                        }
                        break;
                    default:
                        throw new InvalidCSVFormat(line);
                }
            } catch (InvalidCSVFormat e) {
                br.close();
                throw e;
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                br.close();
                throw new InvalidCSVFormat(line);
            }
        }
        br.close();
        return cells;
    }

    public static ArrayList<Monster> readMonsters() throws IOException {
        ArrayList<Monster> monsters = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE_NAME));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            // Limit to 5 parts: type, name, description, role, energy
            // Name or description may contain commas, so we split carefully from the right
            String[] parts = line.split(",");
            try {
                String monsterType = parts[0].trim().toUpperCase();
                // energy is last, role is second to last
                int energy = Integer.parseInt(parts[parts.length - 1].trim());
                Role role = Role.valueOf(parts[parts.length - 2].trim().toUpperCase());
                // description is everything between name and role (index 2 .. parts.length-3)
                String name = parts[1].trim();
                StringBuilder descBuilder = new StringBuilder();
                for (int i = 2; i <= parts.length - 3; i++) {
                    if (i > 2) descBuilder.append(",");
                    descBuilder.append(parts[i].trim());
                }
                String description = descBuilder.toString();

                switch (monsterType) {
                    case "DASHER":
                        monsters.add(new Dasher(name, description, role, energy));
                        break;
                    case "DYNAMO":
                        monsters.add(new Dynamo(name, description, role, energy));
                        break;
                    case "MULTITASKER":
                        monsters.add(new MultiTasker(name, description, role, energy));
                        break;
                    case "SCHEMER":
                        monsters.add(new Schemer(name, description, role, energy));
                        break;
                    default:
                        throw new InvalidCSVFormat(line);
                }
            } catch (InvalidCSVFormat e) {
                br.close();
                throw e;
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                br.close();
                throw new InvalidCSVFormat(line);
            }
        }
        br.close();
        return monsters;
    }
}
