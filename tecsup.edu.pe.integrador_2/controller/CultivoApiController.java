import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cultivos")
public class CultivoApiController {

    private static final Logger logger = LoggerFactory.getLogger(CultivoApiController.class);

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarCultivo(
            @Validated
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        logger.info("===> [guardarCultivo] INICIO");
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("===> [guardarCultivo] No autenticado");
            return ResponseEntity.status(401).body("No autorizado: sesión requerida.");
        }

        String googleId = authentication.getName();
        logger.info("===> [guardarCultivo] googleId extraído: {}", googleId);
        Usuario usuario = usuarioRepository.findByGoogleId(googleId);
        if (usuario == null) {
            logger.warn("===> [guardarCultivo] Usuario no encontrado para googleId: {}", googleId);
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        try {
            logger.info("===> [guardarCultivo] Payload recibido: {}", payload);
            String nombre = (String) payload.get("nombre");
            String descripcion = (String) payload.get("descripcion");
            String localidad = (String) payload.get("localidad");

            Long tipoTerrenoId = Long.parseLong(payload.get("tipoTerrenoId").toString());
            TipoTerreno tipoTerreno = tipoTerrenoRepository.findById(tipoTerrenoId).orElse(null);

            if (tipoTerreno == null) {
                logger.warn("===> [guardarCultivo] Tipo de terreno inválido: {}", tipoTerrenoId);
                return ResponseEntity.status(400).body("Tipo de terreno inválido");
            }

            LocalDate fechaSiembra = null;
            if (payload.get("fechaSiembra") != null) {
                fechaSiembra = LocalDate.parse(payload.get("fechaSiembra").toString());
            }

            Cultivo nuevoCultivo = new Cultivo(nombre, fechaSiembra, descripcion, localidad, usuario, tipoTerreno);
            cultivoRepository.save(nuevoCultivo);

            logger.info("===> [guardarCultivo] Cultivo guardado exitosamente: {}", nuevoCultivo);
            return ResponseEntity.ok(nuevoCultivo);
        } catch (Exception e) {
            logger.error("===> [guardarCultivo] Error al procesar los datos: {}", e.getMessage(), e);
            return ResponseEntity.status(400).body("Error al procesar los datos: " + e.getMessage());
        }
    }
} 