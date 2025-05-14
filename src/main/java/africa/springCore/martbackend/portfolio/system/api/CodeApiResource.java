package africa.springCore.martbackend.portfolio.system.api;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.portfolio.system.domain.model.Code;
import africa.springCore.martbackend.portfolio.system.domain.model.CodeValue;
import africa.springCore.martbackend.portfolio.system.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/codes")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CodeApiResource {
    private final CodeService codeService;

    @GetMapping("")
    public ResponseEntity<BasePageableResponse<Code>> retrieveAllCodes(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "filterValue", required = false) String filterValue
    ) {
        BasePageableResponse<Code> response = codeService.retrieveAllCodes(pageable, filterValue);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{codeId}/codeValues")
    public ResponseEntity<BasePageableResponse<CodeValue>> retrieveAllCodeValuesByCodeId(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name = "codeId") Long codeId
    ) {
        BasePageableResponse<CodeValue> response = codeService.retrieveAllCodeValuesByCodeId(codeId, pageable);
        return ResponseEntity.ok(response);
    }

}
