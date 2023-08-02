package com.eleks.placescanner.place.service.promaptools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.promaptools.Output;
import com.eleks.placescanner.common.domain.promaptools.PromaptoolsResponse;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PromaptoolsServiceTest {

    private final PromaptoolsClient promaptoolsClient = mock(PromaptoolsClient.class);
    private final PromaptoolsService service = new PromaptoolsService(promaptoolsClient);

    @Test
    void shouldReturnLatLongByZipCode() {
        var outputs = List.of(new Output("60090", "30", "50"));
        var expectedResponse = new PromaptoolsResponse(200, outputs);

        when(promaptoolsClient.callLatLngByZip(any())).thenReturn(expectedResponse);

        var actualResponse = service.getLatLongByZip("60090");


        verify(promaptoolsClient, times(1)).callLatLngByZip(any());
        assertEquals(outputs.get(0), actualResponse);
    }

    @Test
    void shouldReturnResourceNotFoundException_WhenOutputsEmpty() {
        var outputs = new ArrayList<Output>();
        var expectedResponse = new PromaptoolsResponse(200, outputs);

        when(promaptoolsClient.callLatLngByZip(any())).thenReturn(expectedResponse);

        assertThrows(ResourceNotFoundException.class, () -> service.getLatLongByZip("60090"));
    }
}