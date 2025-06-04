package persistence;

import models.Crop;
import models.PlantLifeCycle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CropRepository extends EntityRepository implements GenericRepository<Crop> {
    private static CropRepository instance;

    private CropRepository() {super();}

    public static CropRepository getInstance(){
        if(instance == null){
            instance = new CropRepository();
        }
        return instance;
    }

    @Override
    public Crop save(Crop crop) {
        String sql = "INSERT INTO crop VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, crop.getCropId());
            stmt.setString(2, crop.getScientificName());
            stmt.setString(3, crop.getCultivar());
            stmt.setString(4, crop.getCommonName());
            stmt.setString(5, crop.getType().getLiteral());

            stmt.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return crop;
    }

    @Override
    public List<Crop> findAll() {
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM crop"
        )){
            ResultSet rs = statement.executeQuery();
            return new ArrayList<>(mapResultSetToCrop(rs));

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
//
//    public List<Crop> findAllDistinctCrops(){
//        String sql = "SELECT DISTINCT common_name, cultivar FROM crop";
//        try(PreparedStatement)
//    }

    private List<Crop> mapResultSetToCrop(ResultSet rs) throws SQLException {
        List<Crop> crops = new ArrayList<>();
        while(rs.next()){
            crops.add(getOneCropFromResultSet(rs));
        }
        return crops;
    }

    @Override
    public void update(Crop crop) {
        String sql = """
                       UPDATE crop
                       SET scientific_name = ?, cultivar = ?, common_name = ?, life_cycle = ?
                       WHERE crop_id = ?
                """;
        try(PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, crop.getScientificName());
            statement.setString(2, crop.getCultivar());
            statement.setString(3, crop.getCommonName());
            statement.setString(4, crop.getType().getLiteral());
            statement.setString(5, crop.getCropId());

            statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Crop crop) {
        String sql = "DELETE FROM crop WHERE crop_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, crop.getCropId());

            stmt.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Crop getOneCropFromResultSet(ResultSet rs) throws SQLException {
        String cropId = rs.getString("crop_id");
        String scientificName = rs.getString("scientific_name");
        String cultivar = rs.getString("cultivar");
        String commonName = rs.getString("common_name");
        String type = rs.getString("life_cycle");

        Crop crop = new Crop(scientificName, cultivar, commonName, type);
        crop.setCropId(cropId);
        return crop;
    }

    public Optional<Crop> findById(String cropId) {
        String sql = "SELECT * FROM crop WHERE crop_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, cropId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Crop crop = getOneCropFromResultSet(rs);
                return Optional.of(crop);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
