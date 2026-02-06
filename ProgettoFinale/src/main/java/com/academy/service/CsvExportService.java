package com.academy.service;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import com.academy.dto.TripResponseDto;
import com.academy.dto.TripStopResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvExportService {

    private final TripService tripService;

    /**
     * Genera un file CSV contenente i dettagli di un viaggio.
     * Il CSV include informazioni sul viaggio (nome, date, descrizione) 
     * e un elenco delle tappe con città, regione, paese, data visita e note.
     * 
     * @param tripId ID del viaggio da esportare
     * @param username Username dell'utente autenticato (per verificare ownership)
     * @return Array di byte contente il CSV
     * @throws IllegalStateException se la generazione CSV fallisce
     */
    public byte[] generateTripCsv(Long tripId, String username) {
        // Recupera il trip (e valida che appartenga all'utente)
        TripResponseDto trip = tripService.getTripById(tripId, username);

        try {
            // Costruisci il CSV come stringa con StringWriter
            StringWriter stringWriter = new StringWriter();

            // Scrivi intestazione del viaggio
            stringWriter.write("Trip: " + trip.getName() + "\n");
            stringWriter.write("Start Date: " + trip.getStartDate() + "\n");
            stringWriter.write("End Date: " + trip.getEndDate() + "\n");
            stringWriter.write("Description: " + (trip.getDescription() != null ? trip.getDescription() : "") + "\n");
            stringWriter.write("\n");

            // Crea il CSVPrinter per le tappe
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("Order", "City", "Region", "Country", "Visit Date", "Notes")
                    .build();

            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

            // Scrivi i dati delle tappe
            for (TripStopResponseDto stop : trip.getStops()) {
                csvPrinter.printRecord(
                    stop.getOrderInTrip() != null ? stop.getOrderInTrip() : "",
                    stop.getNomeCitta(),
                    stop.getNomeRegione(),
                    stop.getNomePaese(),
                    stop.getVisitDate(),
                    stop.getNotes() != null ? stop.getNotes() : ""
                );
            }

            csvPrinter.flush();
            csvPrinter.close();

            // Converti la stringa a bytes UTF-8 con BOM
            String csvContent = stringWriter.toString();
            byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
            
            // Crea un array con BOM + contenuto
            byte[] result = new byte[csvBytes.length + 3];
            result[0] = (byte) 0xEF;
            result[1] = (byte) 0xBB;
            result[2] = (byte) 0xBF;
            System.arraycopy(csvBytes, 0, result, 3, csvBytes.length);

            return result;

        } catch (Exception e) {
            throw new IllegalStateException("Errore durante la generazione del file CSV", e);
        }
    }
}
